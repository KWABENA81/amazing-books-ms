DROP TABLE IF EXISTS `BOOK_TB`;
CREATE TABLE IF NOT EXISTS `BOOK_TB`
(
    `id`             integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `isbn`           varchar(50)          DEFAULT 'Ako087DEFAULT' NOT NULL,
    `title`          varchar(50)          DEFAULT 'Book Title DEFAULT' NOT NULL,
    `published_date` date                 DEFAULT CURRENT_DATE NOT NULL,
    `total_copies`   integer              DEFAULT 111 NOT NULL,
    `issued_copies`  integer              DEFAULT 1 NOT NULL,
    `author`         varchar(50)          DEFAULT 'Main Author' NOT NULL
);