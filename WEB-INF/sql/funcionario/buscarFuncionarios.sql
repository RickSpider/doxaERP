select funcionarioid as id, nombre as Nombre, apellido as apellido
from funcionarios
where empresaid = ?1
order by funcionarioid asc;