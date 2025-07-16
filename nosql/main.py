from pymongo import MongoClient
from pprint import pprint

# ---------------- Configuração ----------------
URI = "mongodb://admin:admin123@localhost:27017"
client = MongoClient(URI)
db     = client["demo_db"]
col    = db["clientes"]

# Limpa a coleção para rodar o script várias vezes
col.drop()

# 1) Inserção de um dataset (15 registros)
dataset = [
    {"nome": f"Cliente {i}", "idade": 20+i,
    "cidade": "SP" if i % 2 == 0 else "RJ"}
    for i in range(1, 16)
]
insert_res = col.insert_many(dataset)
print(f"➜ Inseridos {len(insert_res.inserted_ids)} documentos")

# 2) Consulta – todos os documentos
print("\n➜ Todos os clientes:")
for doc in col.find({}, {"_id": 0}):# oculta o _id para simplificar
    pprint(doc)

# Consulta – clientes de SP com idade > 25
print("\n➜ Clientes de SP com idade > 25:")
query = {"cidade": "SP", "idade": {"$gt": 25}}
for doc in col.find(query, {"_id": 0}):
    pprint(doc)

# 3) Atualização – aumentar a idade de Cliente 5 em +1
print("\n➜ Atualizando Cliente 5 (idade +1)")
update_filter = {"nome": "Cliente 5"}
update_action = {"$inc": {"idade": 1}}
col.update_one(update_filter, update_action)
pprint(col.find_one(update_filter, {"_id": 0}))

# 4) Exclusão – remover todos de cidade "RJ"
delete_res = col.delete_many({"cidade": "RJ"})
print(f"\n➜ Removidos {delete_res.deleted_count} clientes do RJ")

# Estado final da coleção
print("\n➜ Coleção após exclusões:")
for doc in col.find({}, {"_id": 0}):
    pprint(doc)

# Fechar a conexão (opcional em scripts curtos)
client.close()
