# Gwent-Engine: Simulação Web do Jogo de Cartas

Este projeto é uma implementação de backend para uma versão web do popular jogo de cartas Gwent, encontrado na série de jogos *The Witcher*.

O sistema é dividido em dois módulos principais:
1.  Uma **engine de jogo** em Java puro, que contém todas as regras e a lógica da partida.
2.  Uma **API web** em Spring Boot, que futuramente servirá o jogo para ser jogado em um navegador via WebSockets.

Atualmente, o foco é a simulação da engine de forma isolada através de um terminal interativo, empacotada com Docker para facilitar a execução.

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Build & Dependências:** Maven
* **Engine:** Java Puro (sem frameworks)
* **API:** Spring Boot (em desenvolvimento)
* **Testes:** JUnit 5
* **Containerização:** Docker & Docker Compose

## 📂 Estrutura do Projeto

Este é um projeto Maven multi-módulo:

* **`gwent-engine/`**: Módulo Java puro que contém toda a lógica e regras do jogo. É completamente independente de frameworks web e pode ser executado de forma isolada.
* **`gwent-api-web/`**: Módulo Spring Boot que servirá a API REST (para usuários, decks, etc.) e os WebSockets para a comunicação em tempo real durante o jogo.

## ✅ Pré-requisitos

Antes de começar, garanta que você tem as seguintes ferramentas instaladas na sua máquina:

* [**Git**](https://git-scm.com/downloads) - Para clonar o repositório.
* [**Docker**](https://www.docker.com/products/docker-desktop/) - A plataforma de containerização. O Docker Compose geralmente já vem incluído na instalação do Docker Desktop.

**Você NÃO precisa ter o Java ou o Maven instalados na sua máquina!** O Docker cuida de tudo isso para você.

## 🎮 Como Rodar a Simulação da Engine (Passo a Passo)

Siga estes passos para compilar e executar a simulação interativa da engine do jogo no seu terminal.

### 1. Clone o Repositório
Primeiro, abra seu terminal, navegue até o diretório onde você quer salvar o projeto e clone o repositório do GitHub.

git clone https://github.com/Guga2111/gwent

#### Entre no diretório raiz do projeto
cd gwent-api

### 2. Construa a Imagem Docker
Este comando lê o Dockerfile na raiz do projeto, compila o código Java dentro de um container e cria uma imagem Docker final e pronta para ser executada.

docker-compose build

### 3.Execute a Simulação Interativa
Este é o comando principal. Ele inicia um container a partir da imagem que acabamos de construir e conecta seu terminal diretamente ao programa Java, permitindo que você jogue.

docker-compose run --rm gwent-engine-simulation

### 4. Siga as Instruções no Terminal
Após executar o comando acima, o jogo será iniciado. O terminal mostrará:

* O estado atual da partida (round, placar).
* Sua mão de cartas, com um índice para cada uma.
* Um prompt pedindo sua próxima jogada.
