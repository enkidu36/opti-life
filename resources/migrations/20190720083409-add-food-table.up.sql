CREATE TABLE food
(id SERIAL PRIMARY KEY,
name VARCHAR(50),
food_category category,
calories INTEGER,
carbs INTEGER,
protein INTEGER,
fats INTEGER,
sugars INTEGER,
sodium INTEGER);