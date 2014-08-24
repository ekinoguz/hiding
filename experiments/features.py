import nltk, string
import os, os.path
import cPickle as pickle
import canonizer

class Features:
    """
    List of features we do support
    """
    LETTERUNIGRAM = 0;
    LETTERBIGRAMS = 1;
    POSUNIGRAM = 2;
    POSBIGRAMS = 3;
    LETTERTRIGRAMS = 4;

class FeaturesModel:

    def __init__(self):
        self.FEATURES = set()

    def set_features(self, lst):
        self.list_feature = lst

    def extract(self, text, normalized=True):
        """
        Canonize the text and the extract features.
        Returns a dictionary of <feature, value>

        If we are not debugging/testing, we are going to return
        the features in a format that is suitable for experimenting.
        Basically we are converting dictionary of list into dictionary
        of feature values.
        """
        text = canonizer.canonize(text)
        out = extract(text, self.list_feature, normalized);
        main_dict = {}
        for feat_type in self.list_feature:
            f = out[feat_type]
            if isinstance(f, list):
                for i in range(0, len(f)):
                    main_dict[self.updateFeatures(feat_type, i)] = f[i]
            elif isinstance(f, dict):
                for key in f.keys():
                    main_dict[self.updateFeatures(feat_type, key)] = f[key]
            else:
                print 'unknown feat_type in model_extract:' + str(feat_type)
                sys.exit(-1)
        return main_dict

    def transform_to_sparse(self, main_dict):
        """
        Transform the features in #main_dict into sparse array.
        This transformation will include creating a single array
        that consists of len(array) of features. We will convert
        dictionary into a sparse two-dimensional matrix that is
        suitable for feeding into a classifier.
        """
        # print main_dict
        # vec = DictVectorizer()
        # vectorized = vec.fit_transform(main_dict)
        # print vectorized
        # print vec.get_feature_names()
        # print self.FEATURES
        return False

    def updateFeatures(self, feat_type, key):
        converted = str(feat_type)+'#'+str(key)
        self.FEATURES.add(converted)
        return converted

####################################
### Feature extraction functions ###
####################################
def extract(text, list_feature, normalized=True):
    out = {};

    for feat_type in list_feature:
        if feat_type == Features.LETTERUNIGRAM:
            out[feat_type], count = letterUnigram(text)
        elif feat_type == Features.LETTERBIGRAMS:
            out[feat_type], count = letterBigrams(text)
        elif feat_type == Features.POSUNIGRAM:
            out[feat_type], count = POSUnigrams(text)
        elif feat_type == Features.POSBIGRAMS:
            out[feat_type], count = POSBigrams(text)
        elif feat_type == Features.LETTERTRIGRAMS:
            out[feat_type], count = letterTrigrams(text)
        else:
            print 'unknown feature: ' + str(feat_type) 
            sys.exit(-1)
        if normalized:
            out[feat_type] = normalizeFeature(out[feat_type], count)

    return out

# return a hash map of tag (string) and freq (int)
# possible list of POS: [$ '' ( ) , -- . : CC CD DT EX FW IN JJ JJR JJS LS MD NN 
#                        NNP NNS PDT POS PRP PRP$ RB RBR RBS RP SYM TO UH VB VBD
#                        VBG VBN VBP VBZ WDT WP WP$ WRB ``]
def POSUnigrams(text):
    tokens = nltk.word_tokenize(text)
    pos_tag = nltk.pos_tag(tokens)
    pos_dict = dict(); #map<type of tag (string), freq (int)>
    count = len(pos_tag);
    
    for elt in pos_tag:
        tag = elt[1]
        if tag not in pos_dict:
            pos_dict[tag] = 1;
        else:
            pos_dict[tag] += 1;
    return (pos_dict, count);

def POSBigrams(text):
    tokens = nltk.word_tokenize(text)
    pos_tag = nltk.pos_tag(tokens)
    pos_dict = dict(); #map<type of tag (string), freq (int)>
    count = len(pos_tag)-1;
    
    for i in range(0,len(pos_tag)-1):
        tag1 = pos_tag[i][1]
        tag2 = pos_tag[i+1][1]
        combined_tag = '('+tag1+')-'+'('+tag2+')'
        if combined_tag not in pos_dict:
            pos_dict[combined_tag] = 1;
        else:
            pos_dict[combined_tag] += 1;
    return (pos_dict,count)

# return array of size 26
def letterUnigram(text):
    NUM_CHARS = 26;
    out = [0]*NUM_CHARS;
    count = 0;
    
    for c in text:
        idx = ord(c)-ord('a');
        if idx <= NUM_CHARS-1 and idx >= 0:
            count += 1;
            out[idx] += 1;
      
    return (out,count)

# return a map<type of bigram, freq (int)>
def letterBigrams(text):
    out_dict = dict(); 
    count = 0;
    
    for i in range(0,len(text)-1):
        if text[i].isalpha() and text[i+1].isalpha():
            bigram = text[i]+text[i+1];
            count += 1;
            if bigram in out_dict:
                out_dict[bigram] += 1
            else:
                out_dict[bigram] = 1
    return (out_dict,count)

# return a map<type of trigram, freq (int)>
def letterTrigrams(text):
    out_dict = dict()
    count = 0
    
    for i in range(0,len(text)-2):
        if text[i].isalpha() and text[i+1].isalpha() and text[i+2].isalpha():
            trigram = text[i]+text[i+1]+text[i+2];
            count += 1;
            if trigram in out_dict:
                out_dict[trigram] += 1
            else:
                out_dict[trigram] = 1
    return (out_dict,count)

def normalizeFeature(f, count):
    if count <= 0:
        return f;
    if isinstance(f, list):
        return [float(x)/count for x in f];
    if isinstance(f, dict):
        return dict((key, float(val)/count) for key, val in f.items());
    return f; # should print out error msg??

def save(features, filename):
    """
    We will be using Pickle instead of JSON. Difference is
    Pickle is way much faster, but it will only work within python.
    In other words, we will not be able to use the files within
    other languages, which is not in our future plans.
    """
    with open(filename, 'w') as fp:
        pickle.dump(features, fp)
    return True

def load(filename):
    if not os.path.isfile(filename):
        print filename + " does not exist, what am I gonna read?"
        return False
    with open(filename, 'rb') as fp:
        return pickle.load(fp)
    return False