terraform {
  version = "~> 4.16"
}

provider "aws" {
  region  = "us-west-2"
  access_key = "<access_key>"
  secret_key = "<secret_key>"
}

resource "aws_instance" "app_server" {
  ami           = "ami-08d70e59c07c61a3a"
  instance_type = "t2.micro"

  tags = {
    Name = "ExampleAppServerInstance"
  }
}
