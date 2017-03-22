package syllogisms;

import common.Mood;
import entailment.Conclusion;
import entailment.Conclusions;

/**
 * 
 * 
 * To iterate over the syllogisms:
 * <br \>
 * <code>
 * for (SyllogismEnum syllogism : SyllogismEnum.values()) { }
 * </code>
 * @author Ana Oliveira da Costa
 *
 */
public enum SyllogismEnum {
	
	//Aac: 81
	AA1(new Conclusions(Conclusion.AAC), Mood.A), 
	//Aac: 35, Aca:48
	AA2(new Conclusions(Conclusion.AAC, Conclusion.ACA), Mood.A),
	//Aac: 47, NVC: 31
	AA3(new Conclusions(Conclusion.AAC, Conclusion.NVC), Mood.A),
	//Aac: 49, NVC: 22  -- Iac: 12 
	AA4(new Conclusions(Conclusion.AAC, Conclusion.NVC), Mood.A), 
			//, Conclusion.IAC)),
	
	
	//Iac: 70  --  NVC: 16
	AI1(new Conclusions(Conclusion.IAC), Mood.I),
			//, Conclusion.NVC)),
	//Iac: 20, Ica: 71
	AI2(new Conclusions(Conclusion.IAC, Conclusion.ICA), Mood.I),
	//Ica: 43, NVC: 37  --  Iac: 13
	AI3(new Conclusions(Conclusion.ICA, Conclusion.NVC), Mood.I), 
			//, Conclusion.IAC)),
	//Iac: 54, Ica: 29
	AI4(new Conclusions(Conclusion.IAC, Conclusion.ICA), Mood.I),
	
	
	//Eac: 78
	AE1(new Conclusions(Conclusion.EAC), Mood.E),
	//Eac: 26, Eca: 53  --  NVC: 13
	AE2(new Conclusions(Conclusion.EAC, Conclusion.ECA), Mood.E), 
			//,Conclusion.NVC)),
	//Eac: 33, Eca: 47
	AE3(new Conclusions(Conclusion.EAC, Conclusion.ECA), Mood.E),
	//Eac: 51, NVC: 19  --  Eca: 13
	AE4(new Conclusions(Conclusion.EAC, Conclusion.NVC), Mood.E), 
		//	, Conclusion.ECA)),
	
	//Oac: 62  --  NVC: 14
	AO1(new Conclusions(Conclusion.OAC), Mood.O), 
			//, Conclusion.NVC)),
	//Ica: 24, Oca: 37, NVC: 17
	AO2(new Conclusions(Conclusion.ICA, Conclusion.OCA, Conclusion.NVC), Mood.O),
	//Oca: 40, NVC: 20 --  Oac: 13
	AO3(new Conclusions(Conclusion.OCA, Conclusion.NVC), Mood.O), 
			//, Conclusion.OAC)),
	//Oac: 54 -- NVC: 14
	AO4(new Conclusions(Conclusion.OAC), Mood.O), 
			//, Conclusion.NVC)),
	
	
	//Iac: 82
	IA1(new Conclusions(Conclusion.IAC), Mood.I), 
	//Iac: 27, Ica: 52  -- NVC: 12
	IA2(new Conclusions(Conclusion.IAC, Conclusion.ICA), Mood.I),
			//, Conclusion.NVC)),  
	//Iac: 50, NVC: 28  --  Ica: 12
	IA3(new Conclusions(Conclusion.IAC, Conclusion.NVC), Mood.I),
			//, Conclusion.ICA)), 
	//Iac: 44, Ica: 37
	IA4(new Conclusions(Conclusion.IAC, Conclusion.ICA), Mood.I),
	
	
	//Iac: 61, NVC: 33
	II1(new Conclusions(Conclusion.IAC, Conclusion.NVC), Mood.I),
	//IAC: 25, Ica: 39, NVC: 30
	II2(new Conclusions(Conclusion.IAC, Conclusion.ICA, Conclusion.NVC), Mood.I),
	//IAc: 37, NVC: 51
	II3(new Conclusions(Conclusion.IAC, Conclusion.NVC), Mood.I),
	//Iac: 26, NVC: 61
	II4(new Conclusions(Conclusion.IAC, Conclusion.NVC), Mood.I),
	
	
	//Eac: 24, Oac: 44
	IE1(new Conclusions(Conclusion.EAC, Conclusion.OAC), Mood.E),
	//Eca:29, NVC: 27  -- Eac: 16, Oac: 13
	IE2(new Conclusions(Conclusion.ECA, Conclusion.NVC), Mood.E),
			//, Conclusion.EAC, Conclusion.OAC)),
	//Oac: 20, Eca: 29, NVC: 26  --  Eac:15
	IE3(new Conclusions(Conclusion.OAC, Conclusion.ECA, Conclusion.NVC), Mood.E), 
			//,Conclusion.EAC)),
	//Oac: 28, NVC: 29  --  EAC: 15, ECA:14
	IE4(new Conclusions(Conclusion.OAC, Conclusion.NVC), Mood.E), 
			//, Conclusion.EAC, Conclusion.ECA)),
	
	
	//Oac: 51, NVC: 33
	IO1(new Conclusions(Conclusion.OCA, Conclusion.NVC), Mood.O),
	//NVC: 49 --  Ica: 13, Oca: 15
	IO2(new Conclusions(Conclusion.NVC), Mood.O),
			//, Conclusion.ICA, Conclusion.OCA )),
	//NVC: 53  -- Iac: 12, Oca: 13
	IO3(new Conclusions(Conclusion.NVC), Mood.O),
			//, Conclusion.IAC, Conclusion.OCA)),
	//Oac: 30, NVC: 54
	IO4(new Conclusions(Conclusion.OAC, Conclusion.NVC), Mood.O),

	
	//Eac: 71
	EA1(new Conclusions(Conclusion.EAC), Mood.E), 
	//Eac: 28, Eca: 51 
	EA2(new Conclusions(Conclusion.EAC, Conclusion.ECA), Mood.E),
	//Eac: 63, Eca: 17
	EA3(new Conclusions(Conclusion.EAC, Conclusion.ECA), Mood.E),
	//Eac: 45, Eca: 21  --  NVC: 16
	EA4(new Conclusions(Conclusion.EAC, Conclusion.ECA), Mood.E),
			//, Conclusion.NVC)),
	
	
	//Eac: 53, NVC: 19
	EI1(new Conclusions(Conclusion.EAC, Conclusion.NVC), Mood.E), 
	//Oca: 37, NVC: 17 -- Eac: 16
	EI2(new Conclusions(Conclusion.OCA, Conclusion.NVC), Mood.E),
		//, Conclusion.EAC)), 
	//Eac: 33, Oca: 21, NVC: 21
	EI3(new Conclusions(Conclusion.EAC, Conclusion.OCA, Conclusion.NVC), Mood.E), 
	//Eac: 28, NVC: 32  --  OCA: 15
	EI4(new Conclusions(Conclusion.EAC, Conclusion.NVC), Mood.E), 
			//, Conclusion.OCA)), 
	
	
	//Eac: 46, NVC: 44
	EE1(new Conclusions(Conclusion.EAC, Conclusion.NVC), Mood.E), 
	//Eac: 28, Eca: 22, NVC: 44
	EE2(new Conclusions(Conclusion.EAC, Conclusion.ECA, Conclusion.NVC), Mood.E), 
	//Eac: 17, NVC: 76
	EE3(new Conclusions(Conclusion.EAC, Conclusion.NVC), Mood.E), 
	//Eac: 22, NVC: 66
	EE4(new Conclusions(Conclusion.EAC, Conclusion.NVC), Mood.E), 
	
	
	//Eac: 19, Oac: 24, NVC: 28    
	EO1(new Conclusions(Conclusion.EAC, Conclusion.OAC, Conclusion.NVC), Mood.O), 
	//NVC: 47  --  Eac: 12, Ica: 13, Oca: 15
	EO2(new Conclusions(Conclusion.NVC), Mood.O),
			//, Conclusion.EAC, Conclusion.ICA, Conclusion.OCA )),
	//Eac: 20, NVC: 49
	EO3(new Conclusions(Conclusion.EAC, Conclusion.NVC), Mood.O), 
	//NVC: 57  --  EAC: 15
	EO4(new Conclusions(Conclusion.NVC), Mood.O),
			//, Conclusion.EAC )),
	   
	
	//IAc: 19, Oac: 46, NVC: 20
	OA1(new Conclusions(Conclusion.IAC, Conclusion.OAC, Conclusion.NVC), Mood.O), 
	// Oca: 56 -- NVC: 13
	OA2(new Conclusions(Conclusion.OCA), Mood.O),
		//	, Conclusion.NVC)), 
	//Iac: 19, Oac: 36, NVC: 22
	OA3(new Conclusions(Conclusion.IAC, Conclusion.OAC, Conclusion.NVC), Mood.O), 
	//Oca:42  --  Iac:13, Oac: 12, NVC: 13
	OA4(new Conclusions(Conclusion.OCA), Mood.O), 
			//, Conclusion.IAC, Conclusion.OAC, Conclusion.NVC)), 
	   
	
	//Iac:24, Oac:25, NVC: 36
	OI1(new Conclusions(Conclusion.IAC, Conclusion.OAC, Conclusion.NVC), Mood.O), 
	//Oca:38, NVC:31
	OI2(new Conclusions(Conclusion.OCA, Conclusion.NVC), Mood.O), 
	//Oac:19, NVC:49  --  IAC: 13
	OI3(new Conclusions(Conclusion.OAC, Conclusion.NVC), Mood.O),
			//, Conclusion.IAC )),
	//NVC: 47  -- Ica: 13, Oca:16
	OI4(new Conclusions(Conclusion.NVC), Mood.O),
			//, Conclusion.ICA, Conclusion.OCA)),
	   
	
	//Iac: 17, Oac: 22 NVC: 37
	OE1(new Conclusions(Conclusion.IAC, Conclusion.OAC, Conclusion.NVC), Mood.O), 
	//Eca: 18, NVC: 51
	OE2(new Conclusions(Conclusion.ECA, Conclusion.NVC), Mood.O), 
	//NVC: 47  --  Oac: 13, ECA: 15
	OE3(new Conclusions(Conclusion.NVC), Mood.O), 
	//NVC: 49 -- OAC: 13
	OE4(new Conclusions(Conclusion.NVC), Mood.O), 
	   
	
	//Oac: 47, NVC: 37
	OO1(new Conclusions(Conclusion.OAC, Conclusion.NVC), Mood.O), 
	//Oca: 22, NVC: 42  -- Iac: 13
	OO2(new Conclusions(Conclusion.OCA, Conclusion.NVC), Mood.O),
			//, Conclusion.IAC )),
	//Oac: 22, NVC: 64
	OO3(new Conclusions(Conclusion.OAC, Conclusion.NVC), Mood.O), 
	//Oac: 21, NVC: 66
	OO4(new Conclusions(Conclusion.OAC, Conclusion.NVC), Mood.O);
	
	
	
	public Conclusions peopleConclusions;
	public Mood weakerMood;
	
	SyllogismEnum(Conclusions peopleConclusions, Mood weakerMood) {
		this.peopleConclusions = peopleConclusions;
		this.weakerMood = weakerMood;
	}
	
}
