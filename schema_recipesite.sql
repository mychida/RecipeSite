DROP TABLE IF EXISTS favorites CASCADE;
DROP TABLE IF EXISTS recipes CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL NOT NULL,
  authority VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  username VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY(user_id)
 );
 
 CREATE TABLE IF NOT EXISTS recipes (
   id SERIAL NOT NULL,
   user_id INT NOT NULL,
   recipe_name VARCHAR NOT NULL,
   serving VARCHAR(20) NOT NULL,
   materials VARCHAR(1000) NOT NULL,
   processes VARCHAR(1000) NOT NULL,
   description VARCHAR(1000) NOT NULL,
   tag VARCHAR(30) NOT NULL,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   deleted BOOLEAN NOT NULL,
   PRIMARY KEY(id)
 );
 
 ALTER TABLE recipes ADD CONSTRAINT FK_users_recipes FOREIGN KEY (user_id) REFERENCES users;
 
 CREATE TABLE IF NOT EXISTS favorites (
   id SERIAL NOT NULL,
   user_id INT NOT NULL,
   recipe_id INT NOT NULL,
   created_at TIMESTAMP NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   PRIMARY KEY(id)
 );
 
 ALTER TABLE favorites ADD CONSTRAINT FK_favorites_users FOREIGN KEY (user_id) REFERENCES users;
 ALTER TABLE favorites ADD CONSTRAINT FK_favorites_recipes FOREIGN KEY (recipe_id) REFERENCES recipes;
 
 GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO recipesite;
 GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO recipesite;