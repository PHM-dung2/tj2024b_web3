-- 회원 샘플
INSERT INTO member (memail, mpwd, mname) VALUES
('qwe123', '$2a$10$FlfJ4fQwY9pr5YX.Sw7nBuKB9TXl71m4Bh0Eig.A2ZYlZBmOACk/a', '테스트');

-- 카테고리 테이블 샘플
INSERT INTO category (cname) VALUES
('전자제품'), ('의류'), ('도서'), ('식품');

-- 제품 테이블 샘플
INSERT INTO product (pname, pcontent, pprice, cno) VALUES
('제로펩시', '라임향', '1500', 4 ),
('제로펲시', '라임향', '1500', 4 );