package br.com.fiap.softwell.model

object TipsData {
    val tips = listOf(
        Tip(
            title = "Dicas práticas para o dia a dia",
            summary = "Técnicas simples para reduzir o estresse e melhorar sua concentração no trabalho.",
            details = """
                • Faça pausas regulares de 5 a 10 minutos para descansar a mente e os olhos.
                • Use a técnica 4-7-8 de respiração: inspire contando até 4, segure por 7 segundos e expire contando até 8.
                • Organize seu espaço de trabalho para reduzir distrações.
                • Pratique mindfulness: foque totalmente na tarefa que está fazendo, sem se preocupar com outras coisas.
                • Faça pequenas caminhadas durante o dia para ativar o corpo e aliviar a tensão.
            """.trimIndent()
        ),
        Tip(
            title = "Sinais de alerta",
            summary = "Reconheça sinais de estresse excessivo e burnout para agir a tempo.",
            details = """
                • Sentir-se constantemente cansado e desmotivado.
                • Irritabilidade, dificuldade para dormir ou concentração baixa.
                • Sentimentos de ansiedade ou tristeza que não passam.
                • Falta de interesse em atividades que antes eram prazerosas.

                Caso perceba esses sinais, é importante buscar apoio — seja conversando com alguém de confiança ou utilizando os recursos do app. Você não está sozinho.
            """.trimIndent()
        ),
        Tip(
            title = "Orientações para melhorar o sono",
            summary = "Dicas para criar uma rotina de sono saudável e descansar melhor.",
            details = """
                • Mantenha horários regulares para dormir e acordar, mesmo nos fins de semana.
                • Evite usar telas (celular, computador) pelo menos 1 hora antes de dormir.
                • Crie um ambiente tranquilo e escuro no quarto.
                • Evite cafeína e refeições pesadas perto da hora de dormir.
                • Pratique técnicas de relaxamento antes de dormir, como meditação ou respiração profunda.
            """.trimIndent()
        ),
        Tip(
            title = "Alimentação e exercícios",
            summary = "Como hábitos saudáveis impactam diretamente sua saúde mental.",
            details = """
                • Alimente-se de forma equilibrada, incluindo frutas, legumes, proteínas e grãos integrais.
                • Evite excesso de açúcar e alimentos processados.
                • Pratique exercícios físicos regularmente, mesmo que sejam caminhadas curtas.
                • A atividade física ajuda a liberar endorfinas, que melhoram o humor e reduzem o estresse.
                • Lembre-se: pequenas mudanças já fazem grande diferença na sua saúde mental.
            """.trimIndent()
        )
    )
}
