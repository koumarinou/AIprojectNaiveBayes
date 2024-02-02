import glob
from collections import Counter
import os

def load_texts(directory_path):
    texts = []
    for filename in glob.glob(os.path.join(directory_path, '*.txt')):
        with open(filename, 'r', encoding='utf-8') as file:
            texts.append(file.read())
    return texts

def create_word_frequency_map(texts):
    word_freq = Counter()
    for text in texts:
        words = text.lower().split()
        word_freq.update(words)
    return word_freq

def create_vocabulary(word_freq, m, n, k):
    sorted_words = sorted(word_freq, key=word_freq.get, reverse=True)
    sorted_words = sorted_words[n:]  # Afairoume tis n pio syxnes lexeis 
    sorted_words = sorted_words[:-k] if k > 0 else sorted_words  # Afairoume tis k pio spanies 
    return sorted_words[:m]  # Epilegoume tis m pio suxnes lexeis 

def create_feature_vector(text, vocabulary):
    text_words = set(text.lower().split())
    return [1 if word in text_words else 0 for word in vocabulary]

# Fortosi dedomenwn
positive_texts = load_texts('C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\postrain')
negative_texts = load_texts('C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\negtrain')
positive_texts_test = load_texts('C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\postest')
negative_texts_test = load_texts('C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\negtest')

# Dimiourgia suxnotitas lexewn
positive_freq = create_word_frequency_map(positive_texts)
negative_freq = create_word_frequency_map(negative_texts)

# Sundiasmos suxnotitwn
combined_freq = positive_freq + negative_freq
vocabulary = create_vocabulary(combined_freq, m=500, n=200, k=50)