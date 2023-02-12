DROP TABLE IF EXISTS `BOOK_TB`;
CREATE TABLE IF NOT EXISTS `BOOK_TB`
(
    `id`             integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `isbn`           varchar(50)          DEFAULT 'Ako087DEFAULT',
    `title`          varchar(50)          DEFAULT 'Book Title DEFAULT',
    `published_date` date                 DEFAULT CURRENT_DATE,
    `total_copies`   integer              DEFAULT 111,
    `issued_copies`  integer              DEFAULT 1,
    `author`         varchar(50)          DEFAULT 'Main Author'
);