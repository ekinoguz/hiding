import sys
import operator
import os.path
from math import ceil, floor, fabs, pow
from collections import defaultdict

class DataSet(object):
  """
  Class to represent a dataset, which is a dictionary
  of authors with posts.
  """

  def __init__(self):
    self.posts = defaultdict(list)
    self.divided = defaultdict(list) # divided stands for AR-IR split
    self.split_numbers = defaultdict(list)

    # below should be saved/loaded
    self.authors = []
    self.features = defaultdict(list)
    self.ar_sizes = {}
    self.ir_index = -1

  def add(self, author, post):
    """
    Append @post to @author's posts.
    Add @author to authors set.
    """
    if not author in self.authors:
      self.authors.append(author)
    self.posts[author].append(post)
    return

  def remove(self, author):
    """Remove all posts of a given author.
    """
    self.posts.pop(author)
    self.authors.remove(author)

  def fold_to(self, count):
    """
    Divides each author into #count number of folds
    where each fold has #count number of posts.

    Returns a list of datasets
    """
    datasets = []
    for fold in range(0, count):
      dataset = DataSet()
      dataset.authors = list(self.authors)
      for author in self.posts.keys():
        total = len(self.posts[author])
        size = total / count
        begin = size * fold
        end = size * (fold+1)
        #print author + ' begin:' + str(begin) + ' end:' + str(end)
        if fold == (count-1):
          end = total
        dataset.posts[author] = self.posts[author][begin:end]
      datasets.append(dataset)

    # print '@---> DEBUG: fold_to'
    # print '#folds:', len(datasets)
    # for dataset in datasets:
    #   print '#author:', len(dataset.authors)
    #   for author in dataset.authors:
    #     print '#post:', len(dataset.posts[author])

    self.posts = None
    self.authors = None
    return datasets

  def divide_ar_ir(self, ar_percentage):
    """
    Creates AR and IR set where ar_percentage of posts are
    in AR and the rest is in IR.

    If @ar_percentage is an integer, then AR will be a list
    with [1, ar_percentage] number of reviews concatenated.
    """
    # divided stands for AR-IR split
    # [0] = AR and [1] = IR for convenience
    # Multliple ARs are possible and IR will be at last always.
    self.divided = defaultdict(list)
                      
    self.split_numbers = defaultdict(list)
    self.features = defaultdict(list)
    for author in self.authors:
      total = len(self.posts[author])
      if isinstance(ar_percentage, float):
        ar_sizes = [int(ceil(total * ar_percentage))]
      else:
        ar_sizes = range(1, ar_percentage+1)

      # concatenate the posts
      for ar_size in ar_sizes:
        self.divided[author].append(' '.join(self.posts[author][0:ar_size]))
        self.split_numbers[author].append(ar_size)
        self.features[author].append(None)
      self.divided[author].append(' '.join(self.posts[author][ar_sizes[-1]:total]))
      self.split_numbers[author].append((total - ar_sizes[-1]))
      self.features[author].append(None)

      self.ar_sizes[author] = ar_sizes[-1]

      # make sure total is as expected
      if total != sum(self.split_numbers[author][-2:]):
        print 'sum total:' +  str(sum(self.split_numbers[author][-2:])) + ' should be:' + str(total)
        sys.exit(-1)

    self.ir_index = len(self.divided[author]) - 1

    # print '@---> DEBUG: divide_ar_ir'
    # print '#author:', len(self.divided)
    # for author in self.authors:
    #   print '#total of IR + AR:', len(self.divided[author])
    #   print '#total_post_length:', (sum(len(s) for s in self.posts[author])),\
    #         'ir + max_ar:', (len(self.divided[author][-1]) + len(self.divided[author][-2])),\
    #         'divided:', (sum(len(s) for s in self.divided[author]))
    self.posts = None
    return

  def get_ir(self, author):
    """Return the ir of @author.
    """
    return self.divided[author][-1]

  def get_ars(self, author):
    """Return the ars of @author in a list.
    """
    return self.divided[author][:-1]

  def put_feature(self, author, key, feature):
    """Insert @feature into @author, @key. @key is AR/IR index.
    """
    self.divided[author][key] = None
    self.features[author][key] = feature
    return

  def get_feature(self, author, key):
    return self.features[author][key]

  def get_ar_size(self, author):
    """Return the ar size of @author in a list.
    """
    return self.ar_sizes[author]

  def get_ir_index(self):
    return self.ir_index

class ResultSet:

  LIMIT = 5

  def __init__(self):
    self.results = {} # position of AR among IRs
    self.distances = {} # distance of AR to IR
    self.result_to_distance = {}

    self.sorted_results = defaultdict(list)
    self.sorted_distances = defaultdict(list)

    self.cumulative = {}
    self.top = []

  def add(self, ar, author, result, distance):
    if ar not in self.results:
      self.results[ar] = {}
      self.distances[ar] = {}
      self.result_to_distance[ar] = defaultdict(list)
    self.results[ar][author] = result
    self.distances[ar][author] = distance
    self.result_to_distance[ar][result].append(distance)

  def get_result(self, ar, author):
    return self.results[ar][author]

  def get_distance(self, author):
    return self.distances[author]

  def build_model(self):
    for key in self.results.keys():
      tmp = sorted(self.results[key].iteritems(), key=operator.itemgetter(1))
      for item in tmp:
        self.sorted_results[key].append(item[1])
        self.sorted_distances[key].append(self.distances[key][item[0]])
    # for i in range(0, len(self.sorted_results)):
    #   print str(self.sorted_results[i]) + ':' + str(self.sorted_distances[i])

    # for result in self.result_to_distance.keys():
    #   l = self.result_to_distance[result];
    #   self.result_to_distance[result] = sum(l)/len(l)
    # tmp = sorted(self.result_to_distance.iteritems(), key=operator.itemgetter(0))

    # cumulative_tmp = defaultdict(list)
    
    # for i in range(0, len(tmp)):
    #   cumulative_tmp[i/self.LIMIT].append(tmp[i][1])
    # for i in cumulative_tmp.keys():
    #   l = cumulative_tmp[i]
    #   self.cumulative[i] = sum(l)/len(l)
    
    # build top model
    return


  def test(self, model):
    out = []
    for key in self.results:
      true_result = self.results[key]

      # find the guessed_result from model
      dist = self.distances[key]
      guessed_result = model.find_in_cumulative(dist)
      # print 'true:', true_result, ' -guessed:', guessed_result
      out.append(true_result - guessed_result)
      
    return out

  def find_in_cumulative(self, distance):
    out_dist = "inf"
    returned = -1
    for key in self.cumulative.keys():
      tmp = fabs(distance - self.cumulative[key])
      if tmp < out_dist:
        out_dist = tmp
        returned = key
    return returned * self.LIMIT


  def get_top_result(self, ar, top):
    if isinstance(top, float):
      top = ceil(top * len(self.sorted_results[ar]))

    # print len(self.sorted_results[ar])
    top_x = sum(1 if x < top else 0 for x in self.sorted_results[ar])
    top_x = 100 * top_x / float(len(self.sorted_results[ar]))
    # return 'top-' + str(top) + ':' + str(top_x)
    return str(top_x)

  def extend_sorted_result(self, other_result):
    for key in other_result.sorted_results.keys():
      self.sorted_results[key].extend(other_result.sorted_results[key])
    return

class EvaluationSet:

  def __init__(self, thresholds):
    self.thresholds = thresholds
    self.tr_overs = [0] * len(thresholds)
    self.tr_unders = [0] * len(thresholds)
    self.min_tr_cost = []
    self.te_overs = [0] * 1
    self.te_unders = [0] * 1
    self.min_te_cost = []

  def evaluate(self, cumulative, distance, result, threshold=0.0):
    i = 0
    result = result / ResultSet.LIMIT
    cost = 'inf'
    res = 'inf'
    while i in cumulative:
      if distance < (cumulative[i] + threshold):
        tmp = pow(distance - cumulative[i], 2)
        if tmp < cost:
          cost = tmp
          res = i
      if distance > (cumulative[i] + threshold):
        break

      i += 1
    # print 'found:', i, ' has:', result
    return (result - i)