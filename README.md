Declare synthetic log generator
=======================

A synthetic log generator that generates event logs from Declare process models. The process models can be drawn and altered within the tool.

Its Graphical User Interface is based on the [Declare Designer](http://ceur-ws.org/Vol-489/paper1.pdf) tool. The logs are generated with MINERful, the fast declarative process discovery and simulation tool: It is available for download and fork on the dedicated [MINERful GitHub page](https://github.com/cdc08x/MINERful).
Logs can be exported as [XES](http://www.xes-standard.org/) or [MXML](http://www.processmining.org/logs/mxml) files.

Launch
------------
You need to have a JRE 7+ installed on your machine.
No particular installation procedure is required.

To launch it, import it as an Eclipse project and run the `Declare Designer.launch` file. This version has been tested on a Ubuntu Linux (16.04) and a Windows 7 machine.

Publications and further material
------------
More information is available in these papers:
  - The main simulation algorithm:
  
    Claudio Di Ciccio, Mario Luca Bernardi, Marta Cimitile, Fabrizio Maria Maggi: Generating Event Logs Through the Simulation of Declare Models. EOMAS@CAiSE 2015: 20-36. DOI: [10.1007/978-3-319-24626-0_2](https://doi.org/10.1007/978-3-319-24626-0_2)
    
  - Usage of the algorithm to analise noisy event logs:
  
    Claudio Di Ciccio, Massimo Mecella, Jan Mendling: The Effect of Noise on Mined Declarative Constraints. SIMPDA (Revised Selected Papers) 2013: 1-24. DOI: [10.1007/978-3-662-46436-6_1](https://doi.org/10.1007/978-3-662-46436-6_1)


Contact
------------
Please contact the developers for any information, comment or bug reporting:
[dc.claudio@gmail.com](mailto:dc.claudio@gmail.com)
