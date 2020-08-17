package de.rub.bi.inf.baclient.core.views.extraction;

import javafx.util.Pair;

public class CodeListMapper {
	
	enum BAUWEISE {
		OFFENE_BAUWEISE_EINZELHAUS(1), 
		OFFENE_BAUWEISE_DOPPELHAUS(2), 
		OFFENE_BAUWEISE_HAUSGRUPPE(3), 
		OFFENE_BAUWEISE_GESCHOSSBAU(4), 
		GESCHLOSSENE_BAUWEISE(5), 
		ABWEICHENDE_BAUWEISE(6);
		
		private int value;

        private BAUWEISE(int value) {
                this.value = value;
        }
        
        public int getValue() {
        	return value;
        }
	};
	
	enum BAUMASSNAHMEART {
		ERRICHTUNG(1), 
		AENDERUNG(2), 
		NUTZUNGSAENDERUNG_MIT_BAU(3), 
		NUTZUNGSAENDERUNG_OHNE_BAU(4), 
		BESEITIGUNG(5);
		
		private int value;

        private BAUMASSNAHMEART(int value) {
                this.value = value;
        }
        
        public int getValue() {
        	return value;
        }
	};
	
	enum MBOGEBAEUDEKLASSE {
		GEBAEUDEKLASSE_1(1), 
		GEBAEUDEKLASSE_2(2), 
		GEBAEUDEKLASSE_3(3), 
		GEBAEUDEKLASSE_4(4), 
		GEBAEUDEKLASSE_5(5);
		
		private int value;

        private MBOGEBAEUDEKLASSE(int value) {
                this.value = value;
        }
        
        public int getValue() {
        	return value;
        }
    };
   
}
