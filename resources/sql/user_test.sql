-- :name create-user-test! :<!
-- :doc creates a new user test record
INSERT INTO user_test
(user_id, admin_date, file_content, file_type)
VALUES (:user_id, :admin_date, :file_content, :file_type) RETURNING id

-- :name get-user-test-by-id :? :1
-- :doc retrieve a user-test given id.
SELECT * FROM user_test
WHERE id = :id

-- :name get-user-test-by-user :? :*
-- :doc retrieve all tests for a user
SELECT * from user_test
WHERE user_id = :user_id

-- :name get-user-test-metadata-by-user :? :*
-- :doc retrieve all test metadata for a user
SELECT id, user_id, admin_date, file_type FROM user_test
WHERE user_id = :user_id

-- :name delete-user-test! :! :n
-- :doc delete a user-test given the id
DELETE FROM users_test
WHERE id = :id

