from engine import DataSet
import os, sys, os.path

########################
### Dataset Loaders ####
########################

########
# YELP #
########
YELP_FILE = 'datasets/yelp-rand'
YELP_AUTHOR = 1997
YELP_REVIEW = 1076716

##########
# REDDIT #
##########
REDDIT_FILE = 'datasets/dedupped_num_comments_'

def load(which='yelp', authors='inf', posts='inf'):
  """
  Loads the @which dataset. It can be either 'yelp' or a 
  number to be concatenated to 'reddit' file.

  Returns an instance of Dataset
  """
  filename = YELP_FILE
  if which != 'yelp':
    filename = REDDIT_FILE + str(which) + '.txt'

  filehandler = open(filename, 'r')
  lines = filehandler.readlines()
  filehandler.close()

  data = DataSet()
  i = 0
  while i < len(lines):
    author = lines[i].strip()
    i += 1
    post = lines[i].strip()
    i += 1

    if authors == 'inf' and posts == 'inf':
      data.add(author, post)
      continue

    if posts != 'inf':
      if (len(data.posts[author])) >= posts and len(data.authors) >= authors:
        break
      elif (len(data.posts[author])) >= posts:
        continue
    else:
      if authors != 'inf':
        if len(data.authors) > authors:
          data.remove(author)
          break

    data.add(author, post)

  # check if loaded everything correctly.
  if authors != 'inf' and (len(data.authors) != authors):
    print 'total authors !=:' + str(authors) + ' it is:' + str(len(data.authors))
    sys.exit(-1)

  if posts != 'inf':
    for author in data.authors:
      if len(data.posts[author]) != posts:
        print 'total posts for:' + author + ' !=:' + str(posts) + ' it is:' + str(len(data.posts[author]))
        sys.exit(-1)

  return data

def check_yelp(yelp):
  """
  Make sure number of authors and number of reviews are as expected in yelp
  """
  if (len(yelp.posts) != YELP_AUTHOR):
    print 'number of authors: ' + str(len(yelp.posts)) + ' it should be ' +\
    str(YELP_AUTHOR)
    sys.exit(-1)
    return False

  total = 0
  for author in yelp.posts.keys():
    total += len(yelp.posts[author])
  if (total != YELP_REVIEW):
    print 'number of reviews: ' + str(total) + ' it should be ' +\
    str(YELP_REVIEW)
    sys.exit(-1)
    return False
  return True

############################
# unimportant...............
############################
def unimportant_load(authors, posts, ar_type='test'):
  """type: test | rewritten | translated | fixed
  """
  data = DataSet()

  # load forty_authors from unimportant folder
  forty_authors = get_40_authors()
  for author in forty_authors:
    # get the AR
    ar_filename = author[:-4]
    if ar_type == 'test' or ar_type == 'rewritten':
      ar_filename += ar_type + '-5'
    elif ar_type.startswith('translated'):
      tokenized = ar_type.split('_')
      ar_filename += 'fold-' + str(tokenized[1]) + '-languages-' + str(tokenized[2]) + '-test-5'
    elif ar_type.startswith('fixed'):
      tokenized = ar_type.split('_')
      if len(tokenized) == 1:
        ar_filename += 'translate-5-fixed'
      else:
        ar_filename += tokenized[1]
    else:
      print 'what type of ar is this:', ar_type, '?'
    # print ar_filename
    ar_filename = 'datasets/unimportant/' + ar_filename
    if not os.path.isfile(ar_filename) or not os.access(ar_filename, os.R_OK):
      print ar_filename, 'not found, going to load from original text. This author will should be part of AR.'
      ar_filename = 'datasets/unimportant/' + author[:-4] + 'test-5'

    ar_file = open(ar_filename, 'r')
    for line in ar_file:
      data.add(author, line.strip())
    ar_file.close()

    # get the IR
    authors_file = open('datasets/unimportant/' + author, 'r')
    lines = authors_file.readlines()
    count = 0
    for line in lines:
      if count >= 5:
        data.add(author, line.strip())
      count += 1
      if count == posts:
        break
    authors_file.close()

  # print data.posts['processed-fIfKT5J7OWnr_sNP7VKkAw-clear-rct-only-text'][0]
  # print '--------'
  # print data.posts['processed-fIfKT5J7OWnr_sNP7VKkAw-clear-rct-only-text'][5]

  # read the remaining from all yelp_dataset
  if (len(data.authors) != authors):
    filehandler = open(YELP_FILE, 'r')
    lines = filehandler.readlines()
    filehandler.close()
    i = 0
    while i < len(lines):
      author = lines[i].strip()
      i += 1
      post = lines[i].strip()
      i += 1

      if author in forty_authors:
        continue
      if (len(data.posts[author])) >= posts:
        if len(data.authors) >= authors:
          break
        continue
      data.add(author, post)

  # check if loaded everything correctly.
  if authors != 'inf' and (len(data.authors) != authors):
    print 'total authors !=:' + str(authors) + ' it is:' + str(len(data.authors))
    sys.exit(-1)

  if posts != 'inf':
    for author in data.authors:
      if len(data.posts[author]) != posts:
        print 'total posts for:' + str(authors) + ' !=:' + str(posts) + ' it is:' + str(len(data.posts[author]))
        sys.exit(-1)

  return data

def get_40_authors():
  '''Return list of randomly selected 40 authors.
  '''
  authors = []
  authors_file = open('datasets/unimportant/authors', 'r')
  for line in authors_file:
    authors.append(line.strip()+'-clear-rct-only-text')
  authors_file.close()
  return authors 

def get_fixed_authors():
  '''Return list of authors whose fixing is complete.
  '''
  authors = []
  authors_file = open('datasets/unimportant/authors_fixed', 'r')
  for line in authors_file:
    authors.append(line.strip()+'-clear-rct-only-text')
  authors_file.close()
  return authors 

############################
# end of unimportant........
############################


############################
# some test codes...........
############################
# yelp = load('yelp', 40, 40)
# print len(yelp.authors)
# for author in yelp.authors:
#   print author, len(yelp.posts[author])

# yelp = load('yelp', 40)
# print len(yelp.authors)
# for author in yelp.authors:
#   print author, len(yelp.posts[author])

# yelp = unimportant_load(100, 50)
# print len(yelp.authors)
# for author in yelp.authors:
#   print author, len(yelp.posts[author])

# yelp = unimportant_load(100, 40, 'rewritten')
# print len(yelp.authors)
# for author in yelp.authors:
#   print author, len(yelp.posts[author])
# print yelp.posts['processed-0GHsxjCk-Ynzx4Ihj8tjPQ-clear-rct-only-text'][0]
# print yelp.posts['processed-0GHsxjCk-Ynzx4Ihj8tjPQ-clear-rct-only-text'][1]
# reddit = load('500', 50)
# print len(reddit.authors)
# for author in reddit.authors:
#   print author, len(reddit.posts[author])

# size = 95
# yelp = unimportant_load(40, 100, 'translated_2_10')
# yelp = unimportant_load(40, 100, 'fixed_test-5')
# print yelp.posts['processed-0GHsxjCk-Ynzx4Ihj8tjPQ-clear-rct-only-text'][0]
# yelp = unimportant_load(40, 100, 'fixed_fold-1-languages-9-test-5')
# print yelp.posts['processed-0GHsxjCk-Ynzx4Ihj8tjPQ-clear-rct-only-text'][0]
# yelp = unimportant_load(40, 100, 'translated_1_9')
# print yelp.posts['processed-0GHsxjCk-Ynzx4Ihj8tjPQ-clear-rct-only-text'][0]
# yelp = unimportant_load(40, 100, 'test')
# print yelp.posts['processed-0GHsxjCk-Ynzx4Ihj8tjPQ-clear-rct-only-text'][0]
# yelp = unimportant_load(40, 100, 'rewritten')
# yelp = unimportant_load(40, 100, 'fixed')
# print len(yelp.authors)

# for author in yelp.authors:
#   for fold in range(1, 4):
#     ir = author[:-4] + str(size) + '-' + str(fold)
#     begin = 5 + (fold-1) * 100
#     end = fold * 100
#     out = '\n'.join(yelp.posts[author][begin:end])
#     fopen = open('datasets/created/' + ir, 'w')
#     fopen.write(out)
#     fopen.close()
#     print ir, begin, end
#     for ar in range(1,6):
#       ar_file = author[:-4] + str(size) + '-' + str(fold) + '-test-' + str(ar)
#       begin = (fold-1) * 100
#       end = begin + ar
#       out = '\n'.join(yelp.posts[author][begin:end])
#       print ar_file, begin, end
#       fopen = open('datasets/created/' + ar_file, 'w')
#       fopen.write(out)
#       fopen.close()
  

  