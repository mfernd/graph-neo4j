// list movies of Tom Hanks
MATCH (p:Person)-[a:ACTED_IN]->(m:Movie)
WHERE p.name = "Tom Hanks"
RETURN p.name AS name, m.title AS movie_title;

// list movies between 2 years
MATCH (m:Movie)
WHERE m.released > 2005 AND m.released < 2010
RETURN m.title, m.released;
// RETURN count(1); // and the count

// list all movies with more than 2 actors and return them with their count
MATCH (m:Movie)<-[:ACTED_IN]-(p:Person)
WITH m, COLLECT(p) AS Actors
WHERE size(Actors) > 2
RETURN m.title AS movie_title, size(Actors) AS actors_count;

// add a movie where you are the actor
CREATE (ALambdaMovie:Movie {title: 'A Lambda Movie', released: 2025, tagline: 'CHANGEME'});
CREATE (John:Person {name: 'John Doe', born: 1973});
CREATE (John)-[:ACTED_IN {roles:['Neo']}]->(ALambdaMovie);
MATCH (m:Movie)<-[:ACTED_IN]-(p:Person)
WHERE p.name = "John Doe"
RETURN m, p;
