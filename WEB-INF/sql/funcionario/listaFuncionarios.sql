select funcionarioid, nombre, apellido, documentonum
from funcionarios
where empresaid = ?1
order by funcionarioid asc;