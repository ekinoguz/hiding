import math
import os

class Distances:
  """
  List of distance functions we support
  """
  CHI_SQUARE = 0
  COSINE = 1
  EUCLIDEAN = 2

  def __init__(self):
    self.which = self.CHI_SQUARE
    
  def set_type(self, idx):
    self.which = idx

  def calculate(self, r1, r2):
    """
    r1 and r2 are dictionaries with <feature_name, feature_value>
    which denotes which distance function is going to be used.
    """
    if self.which == Distances.CHI_SQUARE:
      return DistanceFunctions.chi_square(r1, r2)
    elif self.which == Distances.COSINE:
      return DistanceFunctions.cosine(r1, r2)
    elif self.which == Distances.EUCLIDEAN:
      return DistanceFunctions.euclidean(r1, r2)
    else:
      print 'Unknown distance function: ' + str(which)
      sys.exit(-1)


class DistanceFunctions:

  @staticmethod
  def chi_square(r1, r2):
    """
    Canberra (Chi-Square) distance between two dictionaries:
    sum( (xi - yi)^2/(xi + yi) )
    """
    distance = 0.0
    all_keys = set(r1.keys() + r2.keys())

    for key in all_keys:
      x = 0.0
      y = 0.0
      if key in r1:
        x = r1[key]
      if key in r2:
        y = r2[key]
      if x == 0.0 and y == 0.0:
        continue

      distance += math.pow((x - y), 2) / (x + y);

    return distance

  @staticmethod
  def euclidean(r1, r2):
    """
    Histogram distance using L2 metric,(defined as D(x,y) = sum ((xi -yi)^2)
    This is YA distance for Nearest Neighbor algorithms.
    """
    distance = 0.0
    all_keys = set(r1.keys() + r2.keys())

    for key in all_keys:
      v1 = 0
      v2 = 0
      if key in r1:
        v1 = r1[key]
      if key in r2:
        v2 = r2[key]
        
      distance += math.pow((v1-v2), 2)

    return distance

  @staticmethod
  def cosine(r1, r2):
    """
    Cosine Distance or normalized dot product. This is YA distance for Nearest
    Neighbor algorithms, based on John's research at JHU. NOTE: The cosine
    distance was modified slightly as we need to make it nonnegative and we want
    smaller distances to imply similarity.
    """
    distance = 0.0
    h1Magnitude = 0.0
    h2Magnitude = 0.0

    all_keys = set(r1.keys() + r2.keys())
    for key in all_keys:
      r1NormalizedFrequencey = 0.0
      r2NormalizedFrequencey = 0.0;
      if key in r1NormalizedFrequencey:
        r1NormalizedFrequencey = r1[key]
      if key in r2NormalizedFrequencey:
        r2NormalizedFrequencey = r2[key]
          
      distance += r1NormalizedFrequencey * r2NormalizedFrequencey;
      h1Magnitude += r1NormalizedFrequencey * r1NormalizedFrequencey;
      h2Magnitude += r2NormalizedFrequencey * r2NormalizedFrequencey;
    return math.abs((distance / (math.sqrt(h1Magnitude * h2Magnitude))) - 1);
