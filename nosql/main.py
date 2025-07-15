from pymongo import MongoClient
from pprint import pprint

# 1) Conexão -------------------------------------------------------
client = MongoClient("mongodb://admin:admin123@localhost:27017")
db      = client["meu_banco"]
col     = db["pessoas"]

# 2) CREATE  -------------------------------------------------------
joao = {"nome": "João", "idade": 30, "cidade": "São Paulo"}
maria = {"nome": "Maria", "idade": 25, "cidade": "Rio"}
res = col.insert_many([joao, maria])
print("→ Documentos inseridos com _id:", res.inserted_ids)

# 3) READ  ---------------------------------------------------------
print("\nTodos os documentos:")
for doc in col.find():
    pprint(doc)

print("\nSomente quem mora em São Paulo:")
for doc in col.find({"cidade": "São Paulo"}):
    pprint(doc)

# 4) UPDATE --------------------------------------------------------
filtro   = {"nome": "João"}
novos_dados = {"$set": {"idade": 31}}
col.update_one(filtro, novos_dados)
print("\nDepois do update:")
pprint(col.find_one({"nome": "João"}))

# 5) DELETE --------------------------------------------------------
col.delete_one({"nome": "Maria"})
print("\nDepois de deletar Maria, coleção contém:")
for doc in col.find():
    pprint(doc)

# 6) Limpeza opcional (drop da coleção)
#col.drop()
#print("\nColeção limpa.")
