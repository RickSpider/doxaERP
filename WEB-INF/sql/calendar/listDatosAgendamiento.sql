select a.agendamientoid, a.titulo, a.inicio, a.fin, a.contenido
from agendamientos a
where a.empresaid = ?1 
--1 and sucursalid = ?2
order by agendamientoid asc;