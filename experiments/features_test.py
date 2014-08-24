import unittest
import os
import features
from features import Features, FeaturesModel

class FeatureExtractionTests(unittest.TestCase):

    def testLetterUnigram(self):
      text = "ekin"
      results = features.extract(text,[Features.LETTERUNIGRAM],False)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(1, result[ord('e')-ord('a')])
      self.assertEqual(1, result[ord('k')-ord('a')])
      self.assertEqual(1, result[ord('i')-ord('a')])
      self.assertEqual(1, result[ord('n')-ord('a')])
      self.assertEqual(4, sum(result))
      results = features.extract(text,[Features.LETTERUNIGRAM],True)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(1/4.0, result[ord('e')-ord('a')])
      self.assertEqual(1/4.0, result[ord('k')-ord('a')])
      self.assertEqual(1/4.0, result[ord('i')-ord('a')])
      self.assertEqual(1/4.0, result[ord('n')-ord('a')])
      self.assertEqual(4/4.0, sum(result))

      text = ""
      results = features.extract(text,[Features.LETTERUNIGRAM],False)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(0, sum(result))
      results = features.extract(text,[Features.LETTERUNIGRAM],True)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(0, sum(result))

      text = "eee"
      results = features.extract(text,[Features.LETTERUNIGRAM],False)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(3, result[ord('e')-ord('a')])
      self.assertEqual(3, sum(result))
      results = features.extract(text,[Features.LETTERUNIGRAM],True)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(1.0, result[ord('e')-ord('a')])
      self.assertEqual(1.0, sum(result))

      text = "ee k ee"
      results = features.extract(text,[Features.LETTERUNIGRAM],False)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(4, result[ord('e')-ord('a')])
      self.assertEqual(1, result[ord('k')-ord('a')])
      self.assertEqual(5, sum(result))
      results = features.extract(text,[Features.LETTERUNIGRAM],True)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(4/5.0, result[ord('e')-ord('a')])
      self.assertEqual(1/5.0, result[ord('k')-ord('a')])
      self.assertEqual(5/5.0, sum(result))    
      
      text = "\nn"
      results = features.extract(text, [Features.LETTERUNIGRAM], False)
      result = results[Features.LETTERUNIGRAM]
      self.assertEqual(1.0, result[ord('n')-ord('a')])
      self.assertEqual(1.0, sum(result))

    def testLetterBigrams(self):
      text = ""
      results = features.extract(text,[Features.LETTERBIGRAMS],False)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(0, sum(result))
      results = features.extract(text,[Features.LETTERBIGRAMS],True)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(0, sum(result))

      text = "eee"
      results = features.extract(text,[Features.LETTERBIGRAMS],False)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(2, result['ee'])
      self.assertEqual(2, sum(result.values()))
      results = features.extract(text,[Features.LETTERBIGRAMS],True)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(1.0, result['ee'])
      self.assertEqual(1.0, sum(result.values()))

      text = "eee eee e ee"
      results = features.extract(text,[Features.LETTERBIGRAMS],False)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(5, result['ee'])
      self.assertEqual(5, sum(result.values()))
      results = features.extract(text,[Features.LETTERBIGRAMS],True)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(1.0, result['ee'])
      self.assertEqual(1.0, sum(result.values()))

      text = "eekke eee e eze zzkzk"
      results = features.extract(text,[Features.LETTERBIGRAMS],False)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(3, result['ee'])
      self.assertEqual(1, result['ek'])
      self.assertEqual(1, result['kk'])
      self.assertEqual(1, result['ke'])
      self.assertEqual(1, result['ez'])
      self.assertEqual(1, result['ze'])
      self.assertEqual(2, result['zk'])
      self.assertEqual(1, result['kz'])
      self.assertEqual(1, result['zz'])
      self.assertEqual(11+1, sum(result.values()))
      results = features.extract(text,[Features.LETTERBIGRAMS],True)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(3/12.0, result['ee'])
      self.assertEqual(1/12.0, result['ek'])
      self.assertEqual(1/12.0, result['kk'])
      self.assertEqual(1/12.0, result['ke'])
      self.assertEqual(1/12.0, result['ez'])
      self.assertEqual(1/12.0, result['ze'])
      self.assertEqual(2/12.0, result['zk'])
      self.assertEqual(1/12.0, result['kz'])
      self.assertEqual(1/12.0, result['zz'])
      self.assertEqual(1.0, sum(result.values()))

      text = "ababababa"
      results = features.extract(text,[Features.LETTERBIGRAMS],False)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(4, result['ab'])
      self.assertEqual(4, result['ba'])
      self.assertEqual(8, sum(result.values()))
      results = features.extract(text,[Features.LETTERBIGRAMS],True)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(4/8.0, result['ab'])
      self.assertEqual(4/8.0, result['ba'])
      self.assertEqual(1.0, sum(result.values()))
      
      text = "ab \nn \tt"
      results = features.extract(text, [Features.LETTERBIGRAMS], False)
      result = results[Features.LETTERBIGRAMS]
      self.assertEqual(1.0, result['ab'])
      self.assertEqual(1.0, sum(result.values()))

    def testLetterTrigrams(self):
      text = ""
      results = features.extract(text,[Features.LETTERTRIGRAMS],False)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(0, sum(result.values()))
      results = features.extract(text,[Features.LETTERTRIGRAMS],True)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(0, sum(result.values()))

      text = "eee"
      results = features.extract(text,[Features.LETTERTRIGRAMS],False)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(1, result['eee'])
      self.assertEqual(1, sum(result.values()))
      results = features.extract(text,[Features.LETTERTRIGRAMS],True)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(1.0, result['eee'])
      self.assertEqual(1.0, sum(result.values()))

      text = "eee eee e ee"
      results = features.extract(text,[Features.LETTERTRIGRAMS],False)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(2, result['eee'])
      self.assertEqual(2, sum(result.values()))
      results = features.extract(text,[Features.LETTERTRIGRAMS],True)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(1.0, result['eee'])
      self.assertEqual(1.0, sum(result.values()))

      text = "eekke eee e ezee zzkzkz"
      results = features.extract(text,[Features.LETTERTRIGRAMS],False)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(1, result['eek'])
      self.assertEqual(1, result['ekk'])
      self.assertEqual(1, result['kke'])
      self.assertEqual(1, result['eee'])
      self.assertEqual(1, result['eze'])
      self.assertEqual(1, result['zee'])
      self.assertEqual(1, result['zzk'])
      self.assertEqual(2, result['zkz'])
      self.assertEqual(1, result['kzk'])
      self.assertEqual(10, sum(result.values()))
      results = features.extract(text,[Features.LETTERTRIGRAMS],True)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(1/10.0, result['eek'])
      self.assertEqual(1/10.0, result['ekk'])
      self.assertEqual(1/10.0, result['kke'])
      self.assertEqual(1/10.0, result['eee'])
      self.assertEqual(1/10.0, result['eze'])
      self.assertEqual(1/10.0, result['zee'])
      self.assertEqual(1/10.0, result['zzk'])
      self.assertEqual(2/10.0, result['zkz'])
      self.assertEqual(1/10.0, result['kzk'])
      self.assertEqual(10/10.0, round(sum(result.values())))

      text = "ababababa"
      results = features.extract(text,[Features.LETTERTRIGRAMS],False)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(4, result['aba'])
      self.assertEqual(3, result['bab'])
      self.assertEqual(7, sum(result.values()))
      results = features.extract(text,[Features.LETTERTRIGRAMS],True)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(4/7.0, result['aba'])
      self.assertEqual(3/7.0, result['bab'])
      self.assertEqual(1.0, sum(result.values()))
      
      text = "ab \nn \ttzx"
      results = features.extract(text, [Features.LETTERTRIGRAMS], False)
      result = results[Features.LETTERTRIGRAMS]
      self.assertEqual(1.0, result['tzx'])
      self.assertEqual(1.0, sum(result.values()))

    def testPOSUnigrams(self):
      text = "I love you."
      results = features.extract(text, [Features.POSUNIGRAM], False)
      result = results[Features.POSUNIGRAM]
      
      self.assertEqual(1, result['VBP'])
      self.assertEqual(2, result['PRP'])
      self.assertEqual(1, result['.'])

      results = features.extract(text, [Features.POSUNIGRAM], True)
      result = results[Features.POSUNIGRAM]
      self.assertEqual(1/4.0, result['VBP'])
      self.assertEqual(2/4.0, result['PRP'])
      self.assertEqual(1/4.0, result['.'])
      self.assertEqual(1, sum(result.values()))

    def testPOSBIGRAMS(self):
      text = "I love you."
      results = features.extract(text, [Features.POSBIGRAMS], False)
      result = results[Features.POSBIGRAMS]
      self.assertEqual(1, result['(VBP)-(PRP)'])
      self.assertEqual(1, result['(PRP)-(VBP)'])
      self.assertEqual(1, result['(PRP)-(.)'])

      results = features.extract(text, [Features.POSBIGRAMS], True)
      result = results[Features.POSBIGRAMS]
      self.assertEqual(1/3.0, result['(VBP)-(PRP)'])
      self.assertEqual(1/3.0, result['(PRP)-(VBP)'])
      self.assertEqual(1/3.0, result['(PRP)-(.)'])
      self.assertEqual(1, sum(result.values()))

    def testSaveAndLoad(self):
      text = "ekin and ekin."
      results = features.extract(text, 
      [Features.LETTERUNIGRAM, Features.LETTERBIGRAMS], False)

      features.save(results, "test1")
      returned = features.load("test1")
      os.remove("test1")

      self.assertEquals(results, returned)

      self.assertEqual(2, returned[Features.LETTERUNIGRAM][ord('e')-ord('a')])

      results = features.extract(text, 
      [Features.POSBIGRAMS, Features.LETTERBIGRAMS], True)
      features.save(results, "test2")
      returned = features.load("test2")
      os.remove("test2")

      self.assertEquals(results, returned)
      results = features.extract(text, 
      [Features.LETTERUNIGRAM, Features.LETTERBIGRAMS, Features.POSUNIGRAM, Features.POSBIGRAMS], True)
      features.save(results, "test3")
      returned = features.load("test3")
      os.remove("test3")

      self.assertEquals(results, returned)
      
      text = ""
      results = features.extract(text, 
      [Features.LETTERUNIGRAM, Features.LETTERBIGRAMS, Features.POSUNIGRAM, Features.POSBIGRAMS], False)
      
      features.save(results, "test4")
      returned = features.load("test4")
      os.remove("test4")

      self.assertEquals(results, returned)
      
      
      text = "\ttrying to test really long text\n\n \a and see if it still works or not \n 101 531 z3z3\n\t"
      text += text;
      text += text;
      results = features.extract(text, 
      [Features.LETTERUNIGRAM, Features.LETTERBIGRAMS, Features.POSUNIGRAM, Features.POSBIGRAMS], False)
      
      features.save(results, "test5")
      returned = features.load("test5")
      os.remove("test5")

      self.assertEquals(results, returned)
      
      text = ['test1', '222aaa', 'threee333', 't\test1'];
      results['authors'] = [0, 0, 1, 1]
      results['text'] = list();
      for t in text:
        results['text'].append(features.extract(t, [Features.LETTERUNIGRAM, Features.LETTERBIGRAMS, Features.POSUNIGRAM, Features.POSBIGRAMS], False))
      features.save(results, "test6");
      returned = features.load("test6");
      os.remove("test6")
          
      self.assertEqual(results['authors'], returned['authors'])
      
      for idx in range(0,len(returned['text'])):
          feat = features.extract(text[idx], [Features.LETTERUNIGRAM, Features.LETTERBIGRAMS, Features.POSUNIGRAM, Features.POSBIGRAMS], False)
          self.assertEqual(feat, returned['text'][idx])
          

    def testFeaturesModel_Extract(self):
      # model1 = FeaturesModel()
      # text1 = "ekin like."
      # results = model1.extract(text1,
      #       [Features.POSUNIGRAM])
      # print '================'
      # model2 = FeaturesModel()
      # text2 = "ekin."
      # results = model2.extract(text2,
      #       [Features.POSUNIGRAM])

      # results = model1.extract(text1,
      #       [Features.POSUNIGRAM])
      # result = results[Features.LETTERBIGRAMS]
      return True

def main():
    unittest.main()

if __name__ == '__main__':
    main()