INSERT INTO paises (id, nombre) VALUES
(1, 'ARGENTINA'),
(2, 'CHILE'),
(3, 'URUGUAY');

INSERT INTO provincias (pais_id, provincia_id, nombre) VALUES
(1, 1, 'SANTA FE'),
(1, 2, 'CORDOBA'),
(1, 3, 'ENTRE RIOS'),
(2, 1, 'SANTIAGO');


INSERT INTO sequence_table (query_id, ult_valor) VALUES
('paises', 3),
('provincias,PaisId=1', 3),
('provincias,PaisId=2', 1);
