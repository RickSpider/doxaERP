select a.agendamientoid, a.inicio, a.fin, a.titulo, a.contenido, s.nombre, p.razonsocial, t.sigla
from agendamientos a
left join productos s on s.productoid = a.servicioid 
left join personas p on p.personaid = a.personaid
join tipos t on t.tipoid = a.agendamientotipoid
where a.empresaid = ?1 
--and sucursalid = ?2
order by agendamientoid asc;