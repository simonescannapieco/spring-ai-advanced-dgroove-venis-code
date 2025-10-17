#!/bin/bash

model=VitoF/llama-3.1-8b-italian
embedder=hf.co/Simone-Scannapieco/sentence-bert-base-italian-xxl-uncased-F32-GGUF

docker exec -it spring_ai_llm ollama run $model
docker exec -it spring_ai_llm ollama pull $embedder