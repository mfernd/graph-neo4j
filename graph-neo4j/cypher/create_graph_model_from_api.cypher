// https://api.stackexchange.com/2.2/questions?pagesize=100&order=desc&sort=creation&tagged=neo4j&site=stackoverflow&filter=!5-i6Zw8Y)4W7vpy91PMYsKM-k9yzEsSC1_Uxlf
// Fields that are relevant:
// - items[].tags
// - items[].owner.user_id
// - items[].answers[].title
// - items[].answers[].owner.user_id

MATCH(n) DETACH DELETE n;

CALL apoc.load.json('https://api.stackexchange.com/2.2/questions?pagesize=100&order=desc&sort=creation&tagged=neo4j&site=stackoverflow&filter=!5-i6Zw8Y)4W7vpy91PMYsKM-k9yzEsSC1_Uxlf') YIELD value
UNWIND value.items AS questions
UNWIND questions.tags AS questions_tags
UNWIND questions.answers AS answers
MERGE (q:Question {
  id: questions.question_id,
  title: questions.title,
  content: questions.body_markdown
})<-[:ASKED]-(u:User {
  id: questions.owner.user_id,
  name: questions.owner.display_name
})
MERGE (a:Answer {
  id: answers.answer_id,
  title: answers.title,
  content: answers.body_markdown
})<-[:PROVIDED]-(u)
MERGE (q)<-[:ANSWERS]-(a)
MERGE (t:Tag {
  label: questions_tags
})<-[:TAGGED]-(q);
