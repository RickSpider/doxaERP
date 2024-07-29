Select p.personaid, p.razonsocial, td.tipo, p.documentonum 
from personas p 
join tipos td on td.tipoid = p.documentotipoid
where p.empresaid = ?1
order by p.personaid asc;
