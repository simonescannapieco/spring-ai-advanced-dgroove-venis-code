package it.venis.ai.spring.demo.rag;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class TextDataLoader {

    private final VectorStore geminiVectorStore;
    private final VectorStore ollamaVectorStore;

    public TextDataLoader(@Qualifier("geminiVectorStore") VectorStore geminiVectorStore,
            @Qualifier("ollamaVectorStore") VectorStore ollamaVectorStore) {
        this.geminiVectorStore = geminiVectorStore;
        this.ollamaVectorStore = ollamaVectorStore;

    }

    @PostConstruct
    public void loadVenisInfoIntoVectorStore() {
        List<String> venisInfo = List.of(
                "VENIS SpA, acronym for Venice Informatics and Systems, is the ICT services company and local electronic communications operator of the Municipality of Venice, founded on May 22, 1989. With over thirty years of experience in the sector, the company represents the technological operational arm of the Venetian municipal administration, constantly working on the design, development and implementation of the Information System and Telecommunications Network of the lagoon city. The company operates as an in-house provider for the Municipality of Venice and the Metropolitan City, ensuring the digital transformation of public services and technological innovation of the territory.",

                "The legal and main operational headquarters of VENIS SpA is located in the prestigious Palazzo Ziani, situated in Sestiere San Marco 4934 in Venice, with postal code 30124. The company also maintains a presence at VEGA - Venice Science and Technology Park, in the Pleiadi Building in Venice Marghera at Via delle Industrie 27/B. Main contacts include telephone number 041/2744800, fax 041/5238899, email venis@venis.it and certified email protocollo@pec.venis.it. The company is registered in the business register with VAT number 02396850279 and REA number VE-214409.",

                "The corporate structure of VENIS SpA provides for a share capital of 1,549,500 euros fully paid up, divided into 30,000 shares with a nominal value of 51.65 euros each. The company operates under ATECO code 62.02, relating to information technology consultancy, and 62.201 for software production not connected to publishing. Currently the company is led by a Sole Administrator, Paolo Bettio, whose mandate will expire with the approval of the 2025 financial statements. Corporate governance reflects the nature of a public subsidiary company, with the Municipality of Venice as the reference shareholder.",

                "From an economic-financial perspective, VENIS SpA recorded in 2023 a turnover of 21,685,009 euros, confirming its solidity and relevance in the public ICT services landscape. The company has public financial statements available from 2019 to 2024, which demonstrate careful management and constant growth over time. The company employs approximately 835 professionals, as indicated by the company LinkedIn profile, representing one of the main employers in the technology sector of the Venice area. This qualified workforce is distributed among various technical specializations, from application assistance to optical network management.",

                "The corporate mission of VENIS SpA is to bring innovation to serve the city and support the Municipality of Venice, its subsidiary companies and the Metropolitan City in ensuring the right to full digital citizenship. The company positions itself as the system integrator of the City of Venice, with the objective of designing, implementing and managing the solutions and infrastructures that constitute the information system of the local administration. This commitment translates into the creation of increasingly efficient online services, optimization of administrative processes and promotion of digital culture among citizens.",

                "The corporate purpose of VENIS SpA includes the design, implementation, installation and operational management of information systems, including the rationalization of systems already in operation. The company handles the production of operating systems, procedures and electronic programs both basic and application-based, as well as the creation of IT products and services. A fundamental aspect of the activity is the provision of personnel training necessary for the use of implemented technologies, thus ensuring the transfer of digital skills within the public administration and towards citizens.",

                "The main services offered by VENIS SpA include the development and management of the municipal information system to improve Administration processes and equip the Municipality with increasingly simple and efficient tools. The company designs and creates online services with the goal of bringing the Administration closer to citizens, develops network infrastructures for the city including fiber optic broadband and public WiFi, manages video surveillance and networking systems, provides specialized technical assistance, maintains and develops databases, and offers fixed and mobile telephony services for public administration.",

                "The Smart Control Room project represents the flagship of VENIS SpA's technological innovation for the city of Venice. Inaugurated in September 2020 at the Isola Nuova del Tronchetto, this digital control tower was created in collaboration with TIM and Olivetti within an Innovation Partnership financed with over 5 million euros from PON METRO 2014-2020 funds. The operations room constitutes the digital brain of the city, where all metropolitan data relating to land and water mobility, security, tourist flows, weather conditions and public service status converge in real time.",

                "The technological architecture of the Smart Control Room is based on cutting-edge technologies such as artificial intelligence, Internet of Things, cloud computing, Big Data analytics and 5G connectivity. The system uses seven AI algorithms to analyze and correlate data from over 500 cameras distributed throughout the territory, IoT sensors, mobile phone cells and environmental monitoring systems. This infrastructure allows real-time monitoring of up to 430,000 people and 49,000 vessels per day, automatic recognition of types of watercraft, management of tourist flows distinguishing between residents, Italians and foreigners, and prediction of critical situations through predictive analytics.",

                "The Smart Control Room operations involve synergistic collaboration of about thirty operators from different institutions and subsidiary companies: Venice Mobility Agency (ACTV/AVM), Tide Center, Municipality of Venice, Local Police, Civil Protection, VENIS itself and Veritas. Each entity operates according to its own specific competencies but shares information and data in real time, creating a true permanent service conference. This operational integration represents a unique case in the national landscape and required significant coordination work to converge data from different entities with heterogeneous information systems into a single environment.",

                "The Smart City 2.0 model implemented by VENIS SpA in Venice represents an innovative paradigm that surpasses the traditional concept of smart city. The system does not limit itself to monitoring but provides data-driven decision-making tools to the municipal administration, allowing decisions to be made based on real-time data evidence. The platform generates Key Performance Indicators to evaluate the overall state of the city, supports emergency management including high water, optimizes public transport services, and since 2021 also supports the management of tourist access by reservation to protect the fragile Venetian ecosystem.",

                "The open data approach adopted by VENIS SpA ensures that data collected by the Smart Control Room, appropriately anonymized and aggregated in compliance with privacy, is accessible not only to the administration but also to citizens, researchers and businesses. This philosophy of transparency and sharing stimulates the development of new applications and services by third parties, creating a virtuous digital ecosystem. The platform also has a dedicated environment for Open Science Collaboration that allows universities and research institutes to study the characteristic phenomena of the lagoon city using real data.",

                "The results achieved by VENIS SpA in the Smart Cities field have been recognized at national and international levels. Venice was classified as the fourth Smart City in Italy according to the ICityRate 2017 survey by FPA, with absolute first places for the variables 'Urban green' and 'Land consumption', second place for 'Security', and third place for 'Sustainable mobility' and 'Tourism and Culture'. The Smart Control Room project was a finalist at the Digital360 Awards 2021 in the Internet of Things & Big Data Analytics category and received the Digital Agenda Award 2020, confirming the technological excellence achieved.",

                "The network infrastructures managed by VENIS SpA constitute the backbone of city connectivity. The company has created and manages a metropolitan fiber optic network that connects all municipal buildings and subsidiary headquarters, guaranteeing ultra-broadband connectivity. The public city WiFi system offers free Internet access in numerous points of the city, supporting both residents and tourists. The city video surveillance network, integrated with the Smart Control Room, ensures security and continuous monitoring of the territory in compliance with privacy regulations.",

                "VENIS SpA's commitment to training and technology transfer is realized through continuous training programs for municipal and subsidiary staff, ensuring constant updating of digital skills. The company has demonstrated particular attention to specialized training, as evidenced by the collaboration with Dgroove for the delivery of advanced courses on Spring AI and Generative Artificial Intelligence with Java. This training activity is essential to accompany the digital transformation of public administration and ensure that implemented technologies are effectively used to their maximum potential.",

                "VENIS SpA's recruitment strategies reflect the continuous need for specialized skills to support technological innovation. The company regularly conducts selections for professional figures such as IT systems engineers, VMware systems engineers, application assistance technicians, optical network and video surveillance technicians, and networking specialists. Selections take place by qualifications and interview, with public notices available in the Personnel Recruitment section of the company website. This approach ensures transparency in the selection process and attracts qualified talents interested in working in public sector innovation.",

                "The VENIS SpA e-procurement platform represents an example of digitalization of public purchasing processes. The system, accessible through venis.acquistitelematici.it, allows economic operators to register in supplier lists and participate in electronic tender procedures. Integration with NetworkPA allows operators to fill in registration data only once and replicate them on all e-Procurement platforms of the DigitalPA network, significantly simplifying administrative procedures and increasing the efficiency of the procurement process.",

                "VENIS SpA's future vision is oriented towards the further evolution of the Smart City model, with particular attention to artificial intelligence, machine learning and emerging technologies. The company is exploring new AI applications for the prediction and management of typical Venice phenomena, such as high water and tourist flows. The Recovery Fund and European funds represent concrete opportunities to implement new projects already in the design phase. The goal is to consolidate Venice's role as an urban innovation laboratory at the international level, exporting the developed model to other cities with similar characteristics."        
        );
        SearchRequest searchRequest = SearchRequest.builder()
                .query("Check")
                .similarityThresholdAll()
                .build();

        List<Document> similarDocs = geminiVectorStore.similaritySearch(searchRequest);

        if (similarDocs.size() == 0) {

            List<Document> documents =
            venisInfo.stream().map(Document::new).collect(Collectors.toList());
            this.geminiVectorStore.add(documents);

        }

    }

    @PostConstruct
    public void loadSSCVInfoIntoVectorStore() {
        List<String> ssCVInfo = List.of(
                "Simone Scannapieco è un professionista italiano nato il 3 febbraio 1982 a Verona, dove attualmente risiede in Via Amedeo Carisio 14 e mantiene un domicilio in Vicolo Basso Acquar 11. È raggiungibile telefonicamente ai numeri +39 3454076240 e +39 045 508887, e via email all'indirizzo simone.scannapieco@gmail.com. La sua specializzazione professionale si concentra su Ricerca e Sviluppo nel campo delle metodologie semantiche e tecniche all'avanguardia mirate allo sviluppo di prodotti e servizi IT innovativi.",

                "Dal 1° gennaio 2015, Scannapieco ricopre il ruolo di Ricerca e Sviluppo Tecnologico presso Real T S.r.l. di Verona con un contratto a tempo indeterminato. Le sue responsabilità principali includono l'applicazione del Machine Learning per la gestione e il risparmio energetico, lo sviluppo di soluzioni di Machine Learning e Deep Learning per Image Processing e Computer Vision, l'implementazione di tecniche semantiche per sistemi di Open Domain Question Answering, l'analisi e sviluppo di applicazioni per Google Glass, la creazione di soluzioni cloud per la gestione energetica, e progetti di Ambient Semantic Intelligence e Ubiquitous and Pervasive Computing nell'ambito delle Smart Cities.",

                "Prima della posizione attuale, Scannapieco ha lavorato presso Real T S.r.l. dal 15 settembre al 31 dicembre 2014 con un contratto a progetto, concentrandosi sull'analisi di sistemi documentali e sull'applicazione di tecniche semantiche per la normalizzazione, classificazione ed estrazione automatica di dati da documenti. Questo periodo ha rappresentato un importante trampolino di lancio per la sua successiva assunzione a tempo indeterminato nella stessa azienda.",

                "Il percorso formativo di Scannapieco è caratterizzato da un doppio titolo di Dottorato in Informatica conseguito tra il 2010 e il 2014 in regime di co-tutorato internazionale tra il Dipartimento di Informatica dell'Università degli Studi di Verona e l'Institute for Integrated and Intelligent Systems della Griffith University di Brisbane, Australia. La sua tesi dottorale, intitolata 'Towards a methodology for business process revision under norm and outcome compliance', si è concentrata sulla definizione di sistemi formali per specificare processi aziendali e metodologie automatiche per revisionare business processes non conformi rispetto alla normativa vigente o agli obiettivi aziendali.",

                "Durante il percorso di dottorato, Scannapieco ha beneficiato di due importanti borse di studio internazionali nel 2009: una permanenza di breve termine presso il NICTA (National Information and Communications Technology Australia) di Brisbane da settembre a ottobre per l'analisi e produzione scientifica nel contesto della revisione di preferenze in Defeasible Logic, e una borsa di studio presso il Dipartimento di Informatica dell'Università di Verona da giugno ad agosto per lo studio approfondito delle tecniche semantiche di belief revision.",

                "La formazione universitaria di Scannapieco include una Laurea Specialistica in Informatica conseguita nel 2007-2008 presso l'Università di Verona con votazione di 110 e lode, con una tesi di ricerca sulla negoziazione di teorie in Logica Descrittiva che ha sviluppato una metodologia per negoziare modelli logici di conoscenza tra agenti. Ha inoltre conseguito una Laurea Magistrale in Informatica nel periodo 2001-2005 presso la stessa università con votazione 96/110, sviluppando un'applicazione web per la manipolazione di algebre di relazione.",

                "Il percorso scolastico di Scannapieco si è completato con il Diploma di Liceo Scientifico con Piano Nazionale di Informatica conseguito nel 2001 presso il Liceo Scientifico Statale Galileo Galilei di Verona con votazione 77/100, fornendo una solida base nelle discipline scientifiche e nell'informatica fin dagli anni della formazione secondaria.",

                "Le competenze linguistiche di Scannapieco includono l'italiano come lingua madre e un'eccellente padronanza dell'inglese a livello C1 in tutte le competenze: ascolto, lettura, interazione orale, produzione orale e produzione scritta, secondo il Quadro Comune Europeo di Riferimento delle Lingue. Questa competenza linguistica avanzata gli permette di operare efficacemente in contesti internazionali di ricerca e collaborazione.",

                "Sul piano tecnico, Scannapieco possiede un alto grado di preparazione in materia di semantica astratta in molteplici campi tra cui logiche di default, Defeasible Logic, ragionamento non monotono, ragionamento legale, argomentazione semantica, gestione di preferenze e sistemi multiagente. Ha inoltre un'ottima predisposizione nell'applicare metodologie semantiche a contesti applicativi diversi e a supporto di tecnologie all'avanguardia come IoT, Machine Learning e Deep Learning per lo sviluppo di prodotti innovativi.",

                "Le competenze informatiche di Scannapieco includono programmazione avanzata in Java e LaTeX, programmazione a livello intermedio in Python, buone conoscenze nell'utilizzo di IDE come Eclipse, IntelliJ IDEA, PyCharm e Android Studio. Possiede un'ottima conoscenza degli ambienti MacOS e Linux (sia GUI che terminale) e buona conoscenza di Windows 8/10. È esperto nell'uso di software di produttività come suite Office in vari ambienti e degli strumenti G Suite per il cloud computing e la collaborazione.",

                "L'attività di docenza di Scannapieco include corsi specializzati presso istituzioni di formazione superiore. Ha tenuto corsi su 'Spring AI: Generative Artificial Intelligence con Java' sia a livello base che avanzato per VENIS SpA in collaborazione con Dgroove nel 2025, e corsi su 'AI, Generative AI e Large Language Models' presso l'ITS LAST di Verona per diversi curricula nel periodo 2024-2025, dimostrando la sua capacità di trasmettere conoscenze tecniche avanzate a professionisti e studenti.",

                "La partecipazione a conferenze internazionali rappresenta un aspetto significativo dell'attività professionale di Scannapieco. Ha presentato articoli scientifici in prestigiose conferenze come la SAI Computing Conference 2021 a Londra, la International Annual Conference AEIT 2017 a Cagliari, la National Workshop KDWEB 2017, il Salone dell'Innovazione SINNOVA 2015, la RuleML 2014 a Praga, la ICAIL 2013 a Roma e la JURIX 2011 a Vienna, dimostrando un costante impegno nella disseminazione della ricerca scientifica a livello internazionale.",

                "La produzione scientifica di Scannapieco è estremamente prolifica e comprende numerose pubblicazioni in riviste e conferenze internazionali di alto livello. I suoi lavori coprono un ampio spettro di tematiche tra cui la revisione di teorie non-monotone in Defeasible Logic, l'automazione di documenti aziendali basata su estrazione di informazioni, il posizionamento ottimale di telecamere di sicurezza guidato dall'AI, metodologie di Deep Learning per l'agricoltura di precisione, approcci semantici per l'estrazione di dati da documenti non strutturati, analisi forensi di smartphone, clustering automatico di comunità di utenti, trasformazione digitale di documenti finanziari con blockchain, e gestione energetica attraverso Internet of Things e intelligenza artificiale.",

                "Tra le pubblicazioni più recenti e rilevanti di Scannapieco figurano articoli nel Journal of Logic and Computation (2024) sulla revisione di teorie non-monotone, in Expert Systems with Applications (2024) sul metodo Cnosso per l'automazione di documenti aziendali, nelle conferenze KES 2024 sul posizionamento di telecamere di sicurezza, CAFE 2024 su metodologie di Deep Learning in viticoltura, e Applied Sciences (2024) sulla visione artificiale per l'irrorazione intelligente di sostanze chimiche nei vigneti. Questi lavori evidenziano la sua capacità di applicare tecniche avanzate di intelligenza artificiale a problemi pratici in diversi domini.",

                "Le capacità personali e relazionali di Scannapieco includono un'ottima predisposizione alla collaborazione in ambito lavorativo e sociale, combinata con una spiccata attitudine al lavoro di ricerca individuale e autonomo quando necessario. Ha sviluppato buone capacità oratorie in italiano e inglese attraverso l'esperienza universitaria e le numerose collaborazioni internazionali. Possiede inoltre la patente di guida di tipo B, che gli consente mobilità per attività professionali.",

                "L'esperienza di ricerca internazionale di Scannapieco include collaborazioni significative con istituzioni prestigiose come il NICTA in Australia e la Griffith University, dove ha sviluppato competenze avanzate in logiche formali e sistemi intelligenti. Queste esperienze hanno arricchito il suo profilo professionale con una prospettiva globale sulla ricerca in informatica e hanno contribuito alla sua rete di collaborazioni scientifiche internazionali.",

                "Nel campo dell'applicazione pratica delle sue competenze, Scannapieco ha dimostrato la capacità di tradurre concetti teorici avanzati in soluzioni concrete per problemi aziendali reali. Il suo lavoro presso Real T S.r.l. ha prodotto innovazioni significative nell'applicazione del Machine Learning alla gestione energetica, nello sviluppo di sistemi di question answering semantici, e nell'implementazione di soluzioni IoT per Smart Cities, dimostrando un forte orientamento ai risultati e alla creazione di valore attraverso l'innovazione tecnologica.");

        SearchRequest searchRequest = SearchRequest.builder()
                .query("Check")
                .similarityThresholdAll()
                .build();

        List<Document> similarDocs = ollamaVectorStore.similaritySearch(searchRequest);

        if (similarDocs.size() == 0) {

            List<Document> documents = ssCVInfo.stream().map(Document::new).collect(Collectors.toList());

            this.ollamaVectorStore.add(documents);

        }

    }

}
