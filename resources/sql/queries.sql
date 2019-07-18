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

-- :name create-food-category! :! :n
-- :doc creates a new food-category
INSERT INTO food_category
(id, name, description)
VALUES (:id, :name, :description)

-- :name get-food-category :? :1
-- :doc retrieves a food-category by id
SELECT * FROM food-category
WHERE id = :id


-- :name all-food-categories :? :*
-- :desc get all the food categories
SELECT * FROM food_category

-- :name delete-food-category! :! :n
-- :desc delete a food category by id
DELETE FROM food-category
WHERE id = :id