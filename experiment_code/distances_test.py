import unittest
import os
import distances
from distances import Distances

class DistancesTests(unittest.TestCase):

  def testChiSquare(self):
    r1 = {'value1':5}
    r2 = {'value1':5}
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.CHI_SQUARE)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(0, distance)

    r1 = {'value1':5}
    r2 = {'value2':5}
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.CHI_SQUARE)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(10, distance)

    r1 = {'value1':5}
    r2 = {'value1':3}
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.CHI_SQUARE)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(0.5, distance)

    r1 = {'value2':2, 'value3':3}
    r2 = {'value2':6, 'value3':5}
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.CHI_SQUARE)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(2.5, distance)

    r1 = {'value1':1, 'value2':2, 'value3':3}
    r2 = {'value2':6, 'value3':5, 'value4':2}
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.CHI_SQUARE)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(5.5, distance)

  def testEuclidean(self):
    r1 = {'value1':5}
    r2 = {'value2':5}    
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.EUCLIDEAN)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(50, distance)

    r1 = {'value1':5}
    r2 = {'value1':5}
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.EUCLIDEAN)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(0, distance)

    r1 = {'value1':5}
    r2 = {'value1':3}
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.EUCLIDEAN)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(4, distance)

    r1 = {'value1':1, 'value2':2, 'value3':3}
    r2 = {'value1':5, 'value2':6, 'value3':5}
    distanceCalculator = Distances()
    distanceCalculator.set_type(Distances.EUCLIDEAN)
    distance = distanceCalculator.calculate(r1, r2)
    self.assertEqual(36, distance)

def main():
    unittest.main()

if __name__ == '__main__':
    main()