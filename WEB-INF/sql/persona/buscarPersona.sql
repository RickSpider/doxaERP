Select p.personaid as id, p.razonsocial as RazonSocial, td.tipo as TipoDoc, p.documentonum as DocNum 
from personas p 
join tipos td on td.tipoid = p.documentotipoid
where p.empresaid = ?1
order by p.personaid asc;
