-- Inserts sample priorities
INSERT INTO Priority (priority_name) VALUES ('High');
INSERT INTO Priority (priority_name) VALUES ('Medium');
INSERT INTO Priority (priority_name) VALUES ('Low');

-- Inserts sample categories
INSERT INTO Categories (category_name) VALUES ('Work');
INSERT INTO Categories (category_name) VALUES ('School');
INSERT INTO Categories (category_name) VALUES ('Personal');
INSERT INTO Categories (category_name) VALUES ('Health');

-- Inserts sample tasks
INSERT INTO Tasks (description, is_completed, category_id, priority_id) VALUES ('Team meeting', 0, 1, 1);
INSERT INTO Tasks (description, is_completed, category_id, priority_id) VALUES ('Write report', 0, 2, 2);
INSERT INTO Tasks (description, is_completed, category_id, priority_id) VALUES ('Code review', 1, 1, 1);
INSERT INTO Tasks (description, is_completed, category_id, priority_id) VALUES ('Study SQL', 0, 2, 3);
INSERT INTO Tasks (description, is_completed, category_id, priority_id) VALUES ('Plan sprint', 0, 1, 2);
INSERT INTO Tasks (description, is_completed, category_id, priority_id) VALUES ('Grocery Shopping', 1, 3, 2);
INSERT INTO Tasks (description, is_completed, category_id, priority_id) VALUES ('Yoga', 0, 4, 2);