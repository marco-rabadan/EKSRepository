resource "aws_db_instance" "terraform_rds" {
  allocated_storage    = 10
  db_name              = "dbpostgresql"
  engine               = "postgres"
  engine_version       = "14.1"
  instance_class       = "db.t3.micro"
  username             = "foo"
  password             = "foopwd"
  parameter_group_name = "default.mysql5.7"
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