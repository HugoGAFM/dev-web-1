-- Development helper: ensure tb_pedido exists for SQLite
CREATE TABLE IF NOT EXISTS tb_pedido (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  data TEXT NOT NULL,
  produto TEXT NOT NULL,
  preco REAL,
  user_id INTEGER NOT NULL,
  FOREIGN KEY(user_id) REFERENCES tb_user(id)
);
