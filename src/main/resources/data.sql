INSERT INTO blueprints (author, name) VALUES
                                          ('john','house'), ('john','garage'), ('jane','garden')
    ON CONFLICT (author, name) DO NOTHING;

INSERT INTO points (blueprint_id, x, y, seq)
SELECT b.id, p.x, p.y, p.seq
FROM blueprints b
         JOIN (VALUES
                   ('john','house',0,0,0),
                   ('john','house',10,0,1),
                   ('john','house',10,10,2),
                   ('john','house',0,10,3),
                   ('john','garage',5,5,0),
                   ('john','garage',15,5,1),
                   ('john','garage',15,15,2),
                   ('jane','garden',2,2,0),
                   ('jane','garden',3,4,1),
                   ('jane','garden',6,7,2)
) AS p(author,name,x,y,seq) ON b.author = p.author AND b.name = p.name
WHERE NOT EXISTS (
    SELECT 1 FROM points pt WHERE pt.blueprint_id = b.id AND pt.seq = p.seq
);