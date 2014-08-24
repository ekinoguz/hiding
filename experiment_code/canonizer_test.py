import unittest
import canonizer, string

class CanonizeTest(unittest.TestCase):
    
    def testCanonize(self):
        text = ''
        result = canonizer.canonize(text)
        self.assertEqual('', result);
        
        text = 'ab cd. dd f;fae'
        result = canonizer.canonize(text)
        self.assertEqual('ab cd dd ffae', result)
        
        text = 'ALLCAP WITH SPACE1 10'
        result = canonizer.canonize(text)
        self.assertEqual('allcap with space1 10', result)
        
        text = 'ab%d #% c,Cc* *DD *dd'
        result = canonizer.canonize(text)
        self.assertEqual('abd  ccc dd dd', result)
        
        result = canonizer.canonize(string.punctuation)
        self.assertEqual('', result)
        
        result = canonizer.canonize('aa ' + string.punctuation + ' zz' + string.punctuation)
        self.assertEqual('aa  zz', result)

def main():
    unittest.main()

if __name__ == '__main__':
    main()