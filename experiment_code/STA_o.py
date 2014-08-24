#!/usr/bin/python
from collections import defaultdict
from optparse import OptionParser
import features, canonizer, loader
import operator
import cPickle as pickle
from distances import Distances
from engine import DataSet, ResultSet, EvaluationSet
from features import Features, FeaturesModel
from multiprocessing import Pool
import multiprocessing
import math
import numpy as np
# import matplotlib.pyplot as plt
# import matplotlib.cm as cm
import os, os.path
import sys

def get_dataset(FOLD, AR_PERCENTAGE, d_type='yelp', AUTHOR='inf', POST='inf'):
  """
  This loads the dataset and extracts the features. If we have done this
  some time ago and was not lazy enough to save it, then we load it instead
  of doing again, wuhu!
  """
  global AR_TYPE

  # dataset = loader.load(d_type, AUTHOR, POST)
  first_dataset = loader.unimportant_load(AUTHOR, POST * FOLD, AR_TYPE)
  datasets = first_dataset.fold_to(FOLD)
  
  for i in range(0, len(datasets)):
    dataset = datasets[i]
    dataset.divide_ar_ir(AR_PERCENTAGE)
    texts = []

    # check if we have this dataset already calculated.
  
    ir_filename = 'processed/' + get_ir_identifier(d_type, i, AUTHOR, POST)
    ar_filename = 'processed/' + get_ar_identifier(d_type, i, AUTHOR, POST)

    ir_features = None
    if os.path.isfile(ir_filename):
      print '@get: we have the file', ir_filename, 'and going to load it.'
      with open(ir_filename, 'rb') as fp:
        ir_features = pickle.load(fp)
    
    ar_features = None
    if os.path.isfile(ar_filename):
      print '@get: we have the file', ar_filename, 'and going to load it.'
      with open(ar_filename, 'rb') as fp:
        ar_features = pickle.load(fp)


    if ir_features is not None:
      for author in dataset.authors:
        dataset.features[author][-1] = ir_features[author]

    if ar_features is not None:
      for author in dataset.authors:
        dataset.features[author][:-1] = ar_features[author]

    for author in dataset.authors:
      if ar_features is None:
        texts.extend(dataset.get_ars(author))
      if ir_features is None:  
        texts.append(dataset.get_ir(author))

    print '@getting_features, #dataset'#, index_fold
    pool = Pool(processes=NUMBER_OF_CORES)
    it = pool.imap(get_dataset_features, texts)
    pool.close()
    pool.join()

    print '@getting_features FINISHED, adding features to dictionary'
    for author in dataset.authors:
      # for each ar + ir, get back the features
      if ar_features is None:
        for i in range(0, dataset.get_ar_size(author)):
          dataset.put_feature(author, i, it.next())
      if ir_features is None:
        dataset.put_feature(author, dataset.get_ar_size(author), it.next())

    if ir_features is None:
      print '@get: we DONOT have the file', ir_filename, 'is going to be created and saved.'
      with open(ir_filename, 'wb') as fp:
        tmp = dict()
        for key, value in dataset.features.iteritems():
          tmp[key] = value[-1]
        pickle.dump(tmp, fp)

    if ar_features is None:
      print '@get: we DONOT have the file', ar_filename, 'is going to be created and saved.'
      with open(ar_filename, 'wb') as fp:
        tmp = defaultdict(list)
        for key, value in dataset.features.iteritems():
          tmp[key] = value[:-1]
        pickle.dump(tmp, fp)

  return datasets

#############################
# Multiprocessing functions #
#############################
def get_dataset_features(text):
  """Function to be used @get_dataset() for multiprocessing"""
  return model.extract(text)

def process_distance_unknown(tup):
  """Function to be used @process() for multiprocessing"""
  unknown, dataset = tup
  results = []
  ir_index = dataset.get_ar_size(unknown)
  
  for ar_size in range(0, dataset.get_ar_size(unknown)):

    ar = dataset.get_feature(unknown, ar_size)
    ir = dataset.get_feature(unknown, ir_index)
    true_distance = distanceCalculator.calculate(ar, ir)

    true_position = 0
    for known in filter(lambda a: a != unknown, dataset.authors):
      ir = dataset.get_feature(known, ir_index)
      distance = distanceCalculator.calculate(ar, ir)
      if distance < true_distance:
        true_position += 1
    print '@process_distance:', unknown, 'ar', ar_size , 'done true_position:', true_position
    results.append([ar_size, true_position, true_distance])
  return results
#############################
#############################
#############################

def process(dataset, result):
  """
  For each unknown_author, calculate the distance between his AR
  and the IR of each known_author. Then find the true place of
  unknown_author. Save the true place and distance value into @result.
  """
  global AR_TYPE
  if AR_TYPE.startswith('fixed'):
    ar_authors = loader.get_fixed_authors()
  else:
    ar_authors = dataset.authors[0:40]

  tups = []
  for unknown in ar_authors:
    tups.append((unknown, dataset))

  pool = Pool(processes=NUMBER_OF_CORES)
  it = pool.imap(process_distance_unknown, tups)
  pool.close()
  pool.join()

  for unknown in ar_authors:
    distance_results = it.next()
    for distance_result in distance_results:
      [ar_size, position, distance] = distance_result
      result.add(ar_size, unknown, position, distance)
  return

def get_exp_identifier(dataset_type, FOLD, AR, AUTHOR, POST):
  """Return an identifier which is a summary of the experiment."""
  global FEATURES_STR
  global AR_TYPE
  return dataset_type + '-' + AR_TYPE + '-fo' + str(FOLD) + '-fe' +\
      FEATURES_STR + '-ar' + str(AR) + '-a' + str(AUTHOR) + '-p' + str(POST)

def get_ir_identifier(dataset_type, FOLD, AUTHOR, POST):
  """Return an identifier which is a summary of the experiment."""
  global FEATURES_STR
  return dataset_type + '-ir-fo' + str(FOLD) + '-fe' +\
      FEATURES_STR + '-a' + str(AUTHOR) + '-p' + str(POST)

def get_ar_identifier(dataset_type, FOLD, AUTHOR, POST):
  """Return an identifier which is a summary of the experiment."""
  global FEATURES_STR
  global AR_TYPE
  return dataset_type + '-' + AR_TYPE + '-fo' + str(FOLD) + '-fe' +\
      FEATURES_STR + '-a' + str(AUTHOR) + '-p' + str(POST)

def print_result(result):
  global NUMBER_OF_CORES
  global AR_PERCENTAGE
  output = 'AR1, AR2, AR3, AR4, AR5\n'
  tops = [1, 4, 2, 3]
  for top in tops:
    # output += 'Top-' + str(top) + ':'
    for i in range(0, AR_PERCENTAGE):
      output += str(result.get_top_result(i, top)) + ', '
    output += '\n'

  print output
  return output


### Fun Begins Here ###
model = FeaturesModel()
distanceCalculator = Distances()
dataset = DataSet()
NUMBER_OF_CORES = 1
AR_PERCENTAGE = 5
AR_TYPE = 'test'
FEATURES = [Features.LETTERUNIGRAM]
# FEATURES = [Features.LETTERBIGRAMS]
# FEATURES = [Features.LETTERTRIGRAMS, Features.POSBIGRAMS]
# FEATURES = [Features.POSUNIGRAM]
# FEATURES = [Features.POSBIGRAMS]
# FEATURES = [Features.POSBIGRAMS, Features.LETTERTRIGRAMS]
# FEATURES = [Features.LETTERBIGRAMS, Features.POSUNIGRAM]
# FEATURES = [Features.LETTERTRIGRAMS, Features.POSUNIGRAM]
features_str = ''


def main(par_fold=1, par_author=40, par_post=40):
  ################
  ## PARAMETERS ##
  ################
  FOLD = par_fold
  AUTHOR = par_author
  POST = par_post
  DATASET_TYPE = 'yelp'
  # DATASET_TYPE = 'reddit'

  DISTANCE = Distances.CHI_SQUARE

  # Start here...
  model.set_features(FEATURES)
  distanceCalculator.set_type(DISTANCE)

  # Get all the features
  print '#cores:' + str(NUMBER_OF_CORES)
  print '@getting_features'
  datasets = get_dataset(FOLD, AR_PERCENTAGE, DATASET_TYPE, AUTHOR, POST)

  print '@starting the experiments..........................'
  
  results = []
  for i in range(0, len(datasets)):
    print '@fold:', i
    results.append(ResultSet())
    process(datasets[i], results[-1])

  result_avg = ResultSet()
  str_avg = ''
  for i in range(0, len(results)):
    filename = get_exp_identifier(DATASET_TYPE, i, AR_PERCENTAGE, AUTHOR, POST)
    result = results[i]
    result.build_model()
    result_avg.extend_sorted_result(result)
    str_avg += str(i)
    print '@result:', i
    output = filename
    output += print_result(result)
    write_file = open('processed/' + filename + '.result', 'w')
    write_file.write(output)
    write_file.close()

  # get the average of result
  filename = get_exp_identifier(DATASET_TYPE, str_avg, AR_PERCENTAGE, AUTHOR, POST)
  print '@result average'
  output = print_result(result_avg)
  print 'Results are written to:' + filename
  write_file = open('processed/' + filename + '.result', 'w')
  write_file.write(output)
  write_file.close()

  return

if __name__ == '__main__':
  parser = OptionParser()
  parser.add_option("-c", "--cores", dest="cores", default=1,
                    help="number of cores to be used", metavar="<number>")
  parser.add_option("-a", "--authors", dest="authors", default=40,
                    help="number of authors to be used", metavar="<number>")
  parser.add_option("-p", "--posts", dest="posts", default=40,
                    help="number of posts to be used", metavar="<number>")
  parser.add_option("-v", "--folds", dest="folds", default=1,
                    help="number of folds to be used", metavar="<number>")
  parser.add_option("-r", "--ar", dest="ar", default=5,
                    help="number of ARs to be used", metavar="<number>")
  parser.add_option("-f", "--features", dest="features", default=0,
                    help="features to be used. LU=0, LB=1, LT=4, PU=2, PB=3", metavar="[features]")
  parser.add_option("-t", "--ar_type", dest="ar_type", default='test',
                    help="type of AR to be used", metavar="<test|rewritten|translated_f_l|fixed>")
  parser.add_option("-q", "--quiet",
                    action="store_false", dest="verbose", default=True,
                    help="don't print status messages to stdout")

  (options, args) = parser.parse_args()
  
  global NUMBER_OF_CORES
  NUMBER_OF_CORES = int(options.cores)
  if NUMBER_OF_CORES == 1:
    NUMBER_OF_CORES = multiprocessing.cpu_count()

  global FEATURES
  FEATURES = map(int, list(str(options.features)))

  global FEATURES_STR
  FEATURES_STR = "[{}]".format("_".join(map(repr, FEATURES)))
  FEATURES_STR = FEATURES_STR[1:len(FEATURES_STR)-1] # get rid of '[' & ']'

  global AR_PERCENTAGE
  AR_PERCENTAGE = int(options.ar)

  global AR_TYPE
  AR_TYPE = str(options.ar_type)

  main(int(options.folds), int(options.authors), int(options.posts))