resource "aws_sqs_queue" "terraform_queue" {
  name                        = "terraform-example-queue.fifo"
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