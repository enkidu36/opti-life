CREATE TABLE user_test
(id SERIAL PRIMARY KEY,
user_id VARCHAR(20) REFERENCES users(id),
admin_date DATE);

--;;

CREATE TABLE test_detail
(id SERIAL PRIMARY KEY,
food_id INTEGER REFERENCES food(id),
user_test_id INTEGER REFERENCES user_test(id),
reactivity reactivity_key);