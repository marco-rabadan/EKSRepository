resource "aws_sqs_queue" "ticket_events" {
  name                        = "ticket_events.fifo"
  fifo_queue                  = true
  content_based_deduplication = true
}
resource "aws_sqs_queue" "order_events" {
  name                        = "order_events.fifo"
  fifo_queue                  = true
  content_based_deduplication = true
}
resource "aws_sqs_queue" "payment_events" {
  name                        = "payment_events.fifo"
  fifo_queue                  = true
  content_based_deduplication = true
}

output "ticket_events_url" {
  value = aws_sqs_queue.ticket_events.url
}
output "ticket_events_arn" {
  value = aws_sqs_queue.ticket_events.arn
}
output "ticket_events_id" {
  value = aws_sqs_queue.ticket_events.id
}

output "order_events_url" {
  value = aws_sqs_queue.ticket_events.url
}
output "order_events_arn" {
  value = aws_sqs_queue.ticket_events.arn
}
output "order_events_id" {
  value = aws_sqs_queue.ticket_events.id
}

output "payment_events_url" {
  value = aws_sqs_queue.ticket_events.url
}
output "payment_events_arn" {
  value = aws_sqs_queue.ticket_events.arn
}
output "payment_events_id" {
  value = aws_sqs_queue.ticket_events.id
}