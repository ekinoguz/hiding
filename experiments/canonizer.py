import string

# for now just 1) convert to lowercase. 2) strip punctuation
def canonize(text):
    text = text.lower()
    text = text.translate(None, string.punctuation)
    return text