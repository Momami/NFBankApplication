from gensim.models.keyedvectors import Word2VecKeyedVectors
import json
from nltk.stem.snowball import RussianStemmer
from nltk.corpus import stopwords
import nltk
import string
import pymorphy2


nltk.download('stopwords')
cachedStopWords = stopwords.words("russian")
nltk.download('punkt')
porter_stemmer = RussianStemmer()
morph = pymorphy2.MorphAnalyzer()
punct = string.punctuation


def preparation_docs(docs):
    for i, field in enumerate(docs):
        nltk_tokens = nltk.word_tokenize(field)
        nltk_tokens = [morph.parse(word)[0].normal_form for word in nltk_tokens
                       if word not in cachedStopWords and word not in punct]
        nltk_tokens = ' '.join(nltk_tokens)
        docs[i] = nltk_tokens

    return docs


model = Word2VecKeyedVectors.load_word2vec_format\
    ('C:/Users/Rabbit/PycharmProjects/mil/mil/word2vec_model.bin', binary=True)
with open('C:/Users/Rabbit/PycharmProjects/mil/mil/readertown.json', "r", encoding="utf-8") as read_file:
    data = json.load(read_file)
words = set()
elements = [elem.get("title") for elem in data if elem.get("title") is not None]
elements.extend([elem.get("publisher") for elem in data if elem.get("publisher") is not None])
elements = set(elements)
docs = preparation_docs(list(elements))
for elem in docs:
    words.update(set(elem.split(" ")))
# синонимы
syn = dict()
for word in words:
    if word in model:
        ans = model.most_similar(positive=[word])
        syn[word] = [name for name, vs in ans]
with open("synonym1.txt", "w") as file:
    for key, val in syn.items():
        file.write(key + "|" + ' '.join(val) + "\n")
