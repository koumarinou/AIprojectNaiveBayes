import os
import glob
from collections import Counter
import numpy as np
from sklearn.naive_bayes import MultinomialNB
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split

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
    sorted_words = sorted(word_freq.items(), key=lambda x: x[1], reverse=True)
    sorted_words = [word for word, freq in sorted_words[n:]]  # Remove n most frequent
    sorted_words = sorted_words[:-k] if k > 0 else sorted_words  # Remove k least frequent
    return sorted_words[:m]  # Select m most frequent

def create_feature_vector(text, vocabulary):
    text_words = set(text.lower().split())
    return np.array([1 if word in text_words else 0 for word in vocabulary])

# Fortosi dedomenwn
positive_texts = load_texts('C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\postrain')
negative_texts = load_texts('C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\negtrain')
positive_texts_test = load_texts('C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\postest')
negative_texts_test = load_texts('C:\\Users\\eirin\\OneDrive - aueb.gr\\Desktop\\ergasiatexniti\\negtest')


all_texts = positive_texts + negative_texts
all_texts_test = positive_texts_test + negative_texts_test

word_freq = create_word_frequency_map(all_texts + all_texts_test)
vocabulary = create_vocabulary(word_freq, m=500, n=200, k=50)
X = np.array([create_feature_vector(text, vocabulary) for text in all_texts])
X_test = np.array([create_feature_vector(text, vocabulary) for text in all_texts_test])
y = np.array([1] * len(positive_texts) + [0] * len(negative_texts))
y_test = np.array([1] * len(positive_texts_test) + [0] * len(negative_texts_test))

# Initialize lists to store metrics
training_sizes = np.arange(50, 601, 50)  
train_accuracies = []
test_accuracies = []
precision_values = []
recall_values = []
f1_values = []



for size in training_sizes:
    X_train_sub, _, y_train_sub, _ = train_test_split(X, y, train_size=size, stratify=y, random_state=50)
    model = MultinomialNB()
    model.fit(X_train_sub, y_train_sub)

    # Training accuracy
    y_pred_train = model.predict(X_train_sub)
    train_accuracies.append(accuracy_score(y_train_sub, y_pred_train))

    # Test accuracy
    y_pred_test = model.predict(X_test)
    test_accuracies.append(accuracy_score(y_test, y_pred_test))

    # Precision, Recall, F1 for training data
    precision_values.append(precision_score(y_train_sub, y_pred_train))
    recall_values.append(recall_score(y_train_sub, y_pred_train))
    f1_values.append(f1_score(y_train_sub, y_pred_train))


for size in train_accuracies:
    print("Train")
    print(size)
for size in test_accuracies:
    print("Test")
    print(size)
for size in precision_values:
    print("Pre")
    print(size)
for size in recall_values:
    print("Recall")
    print(size)
for size in f1_values:
    print("F1")
    print(size)

# Plotting
plt.figure(figsize=(14, 7))

# Accuracy Plot
plt.subplot(1, 2, 1)
plt.plot(training_sizes, train_accuracies, label='Training Accuracy', marker='o', linestyle='-')
plt.plot(training_sizes, test_accuracies, label='Test Accuracy', marker='x', linestyle='-')
plt.xticks(training_sizes)  
plt.xlabel('Training Size')
plt.ylabel('Accuracy')
plt.title('Training Size vs Accuracy')
plt.legend()

# Precision, Recall, F1 Plot
plt.subplot(1, 2, 2)
plt.plot(training_sizes, precision_values, label='Precision', marker='o', linestyle='-')
plt.plot(training_sizes, recall_values, label='Recall', marker='x', linestyle='-')
plt.plot(training_sizes, f1_values, label='F1 Score', marker='^', linestyle='-')
plt.xticks(training_sizes)  
plt.xlabel('Training Size')
plt.ylabel('Score')
plt.title('Training Size vs Precision, Recall, F1 Score')
plt.legend()

plt.tight_layout()
plt.show()