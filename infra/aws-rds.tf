resource "aws_db_instance" "terraform_rds" {
  allocated_storage    = 10
  db_name              = "db_postgres_${var.environment}"
  engine               = "postgres"
  engine_version       = "14.1"
  instance_class       = "db.t3.micro"
  username             = "postgresql1"
  password             = "foopwdpost!"
  publicly_accessible   = true
  tags = {
    Name = "db_postgres_${var.environment}"
  }
}

output "rdsdbname" {
  value = aws_db_instance.terraform_rds.db_name
}
output "rdsarn" {
  value = aws_db_instance.terraform_rds.arn
}
output "rdsid" {
  value = aws_db_instance.terraform_rds.id
}
output "rdsendpoint" {
  value = aws_db_instance.terraform_rds.endpoint
}