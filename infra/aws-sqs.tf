resource "aws_sqs_queue" "terraform_queue" {
  name                        = "terraform-example-queue.fifo"
  fifo_queue                  = true
}

resource "aws_sqs_queue" "terraform_queue_test" {
  name                        = "terraform_queue_test.fifo"
  fifo_queue                  = true
}

output "sqsurl" {
  value = aws_sqs_queue.terraform_queue.url
}
output "sqsarn" {
  value = aws_sqs_queue.terraform_queue.arn
}
output "sqsid" {
  value = aws_sqs_queue.terraform_queue.id
}

output "sqstestid" {
  value = aws_sqs_queue.terraform_queue_test.id
}