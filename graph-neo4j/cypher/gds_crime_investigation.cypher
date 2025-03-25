// Give me the details of all the Crimes under investigation by Officer Larive (Badge Number 26-5234182)
MATCH (c:Crime {last_outcome: "Under investigation", type: "Drugs"})-[i:INVESTIGATED_BY]->(o:Officer {badge_no: "26-5234182", surname: "Larive"})
RETURN *;

// Get paths between 2 persons
MATCH p = SHORTEST 1 (p1:Person)-[r:KNOWS|KNOWS_LW|KNOWS_PHONE|KNOWS_SN]-+(p2:Person)
WHERE p1.name = "Jack" AND p2.name = "Raymond"
RETURN [n in nodes(p) | n.name] AS stops
ORDER BY size(stops) ASC;
