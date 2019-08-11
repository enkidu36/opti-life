-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, pass)
VALUES (:id, :pass)

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id

-- :name create-food :! :n
-- :doc creates a new food
INSERT INTO food
(name, food_category, calories, carbs, fats, protein, sugars, sodium)
VALUES (:name, :food_category ::category, :calories, :carbs, :fats, :protein, :sugars, :sodium)

-- :name get-food :? :1
-- :doc get a food by id
SELECT * FROM food
WHERE id = :id

-- :name all-foods :? :*
-- :doc retrieves all the foods
SELECT id, name, food_category FROM food

-- :name delete food! :! :n
-- :doc delete food
DELETE FROM food
WHERE id = :id

