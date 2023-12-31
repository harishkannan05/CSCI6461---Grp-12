
 //Class CPU is used to create registers and a memory object
import java.io.*;
import java.util.Arrays;

public class CPU {
    
    Register PC = new Register(12);
    Register CC = new Register(4);
    Register IR = new Register(16);
    Register MAR = new Register(12);
    Register MBR = new Register(16);
    Register MFR = new Register(4);
    Register X1 = new Register(16);
    Register X2 = new Register(16);
    Register X3 = new Register(16);
    Register GPR0 = new Register(16);
    Register GPR1 = new Register(16);
    Register GPR2 = new Register(16);
    Register GPR3 = new Register(16);
    Register HLT = new Register(1);
    
    Memory main_Memory = new Memory();

    
    //Function to execute a Single Step or Run
    public void execute(String type){
        
        if ("single".equals(type)){
            //Using PC we get the instruction location and add it to IR
            int[] instruction_address = getRegisterValue("PC");
            int converted_address = binaryToInt(instruction_address);
            setRegisterValue("IR",getMemoryValue(converted_address)); 
            
            //Increment PC 
            int[] cur_PC = getRegisterValue("PC");
            int trans_PC = binaryToInt(cur_PC);
            trans_PC = trans_PC+1;
            int[] new_PC = intToBinaryArrayShort(Integer.toBinaryString(trans_PC));
            setRegisterValue("PC", new_PC);
            
            
            //Read and decode instruction
            int[] instructionBinary = getMemoryValue(converted_address);
            int[] OPcode = Arrays.copyOfRange(instructionBinary, 0, 6);
            String instruction = decodeOPCode(OPcode);
            
            //Execute opcode instruction
            if ("LDR".equals(instruction)){     //Load Opcode LDR
                
                int[] result = computeEffectiveAddr(instructionBinary);
                int EA = result[0];
                int I= result[1];
                int R= result[2];
                int IX= result[3];
                
                //Set MAR to the location in memory to fetch
                int Addr= result[4];
                setRegisterValue("MAR", intToBinaryArrayShort(Integer.toBinaryString(EA)));
                
                switch(R) {
                    case 0:
                        //Set MBR to the value to be stored in register
                        setRegisterValue("MBR", getMemoryValue(EA));
                        //Set register to value from MBR
                        GPR0.setRegisterValue(MBR.getRegisterValue());
                        break;
                    case 1:
                        //Set MBR to the value to be stored in register
                        setRegisterValue("MBR", getMemoryValue(EA));
                        //Set register to value from MBR
                        GPR1.setRegisterValue(MBR.getRegisterValue());
                        break;
                    case 2:
                        //Set MBR to the value to be stored in register
                        setRegisterValue("MBR", getMemoryValue(EA));
                        // Set register to value from MBR
                        GPR2.setRegisterValue(MBR.getRegisterValue());
                        break; 
                    default:
                        //Set MBR to the value to be stored in register
                        setRegisterValue("MBR", getMemoryValue(EA));
                        //Set register to value from MBR
                        GPR3.setRegisterValue(MBR.getRegisterValue());
                 }
                
                //Set MBR to value we just fetched
                setRegisterValue("MBR", getMemoryValue(EA));
                
            }else if("STR".equals(instruction)){	//Load Opcode STR
                
                int[] result = computeEffectiveAddr(instructionBinary);
                int EA = result[0];
                int I= result[1];
                int R= result[2];
                int IX= result[3];
                int Addr= result[4];
                
                setRegisterValue("MAR", intToBinaryArrayShort(Integer.toBinaryString(EA)));
                switch(R) {
                    case 0:
                        //Set MBR to the value to be stored in memory
                        setRegisterValue("MBR", GPR0.getRegisterValue());
                        //Store value from MBR
                        setMemoryValue(EA,MBR.getRegisterValue());
                        break;
                    case 1:
                        //Set MBR to the value to be stored in memory
                        setRegisterValue("MBR", GPR1.getRegisterValue());
                        //Store the value from MBR
                        setMemoryValue(EA,MBR.getRegisterValue());
                        break;
                    case 2:
                        //Set MBR to the value to be stored in memory
                        setRegisterValue("MBR", GPR2.getRegisterValue());
                        //Store value from MBR
                        setMemoryValue(EA,MBR.getRegisterValue());
                        break; 
                    default:
                        //Set MBR to the value to be stored in memory
                        setRegisterValue("MBR", GPR3.getRegisterValue());
                        //Store value from MBR
                        setMemoryValue(EA,MBR.getRegisterValue());
                 }
            }else if("LDA".equals(instruction)){	//Load Opcode LDA
                int[] result = computeEffectiveAddr(instructionBinary);
                int EA = result[0];
                int I= result[1];
                int R= result[2];
                int IX= result[3];
                int Addr= result[4];
                
                setRegisterValue("MAR", intToBinaryArrayShort(Integer.toBinaryString(EA)));
                int[] converted_value = intToBinaryArray(Integer.toBinaryString(EA));
                switch(R) {
                    case 0:
                    	//Store MAR value is R0
                        GPR0.setRegisterValue(converted_value);
                        break;
                    case 1:
                    	//Store MAR value is R1
                        GPR1.setRegisterValue(converted_value);
                        break;
                    case 2:
                    	//Store MAR value is R2
                        GPR2.setRegisterValue(converted_value);
                        break; 
                    default:
                    	//Store MAR value is R3
                        GPR3.setRegisterValue(converted_value);
                 }
            }else if("LDX".equals(instruction)){	//Load Opcode LDX
                int[] result = computeEffectiveAddr(instructionBinary);
                int EA = result[0];
                int I= result[1];
                int R= result[2];
                int IX= result[3];
                int Addr= result[4];
                
                setRegisterValue("MAR", intToBinaryArrayShort(Integer.toBinaryString(EA)));
                switch(IX) {
                    case 1:
                        //Set MBR to the value loaded from memory
                        setRegisterValue("MBR", getMemoryValue(EA));
                        //Load MBR into index register
                        X1.setRegisterValue(MBR.getRegisterValue());
                        break;
                    case 2:
                        //Set MBR to the value loaded from memory
                        setRegisterValue("MBR", getMemoryValue(EA));
                        //Load MBR into index register
                        X2.setRegisterValue(MBR.getRegisterValue());
                        break;
                    case 3:
                        //Set MBR to the value loaded from memory
                        setRegisterValue("MBR", getMemoryValue(EA));
                        //Load MBR into index register
                        X2.setRegisterValue(MBR.getRegisterValue());
                        break;
                    default:
                 }
            }else if("STX".equals(instruction)){	//Load Opcode STX
                int[] result = computeEffectiveAddr(instructionBinary);
                int EA = result[0];
                int I= result[1];
                int R= result[2];
                int IX= result[3];
                int Addr= result[4];
                
                setRegisterValue("MAR", intToBinaryArrayShort(Integer.toBinaryString(EA)));
                switch(IX) {
                    case 1:
                        //Set MBR to the word to be stored in memory
                        setRegisterValue("MBR", X1.getRegisterValue());
                        //Store value from MBR
                        setMemoryValue(EA,MBR.getRegisterValue());
                        break;
                    case 2:
                        //Set MBR to the word to be stored in memory
                        setRegisterValue("MBR", X2.getRegisterValue());
                        // Store value from MBR
                        setMemoryValue(EA,MBR.getRegisterValue());
                        break;
                    case 3:
                        //Set MBR to the word to be stored in memory
                        setRegisterValue("MBR", X3.getRegisterValue());
                        //Store value from MBR
                        setMemoryValue(EA,MBR.getRegisterValue());
                        break; 
                    default:
                }
            }else if("HLT".equals(instruction)){	//Load Opcode HLT
                int [] msg = new int[]{1};
                HLT.setRegisterValue(msg);
            }
        }
    }
    
    //Function to compute the EA of an instruction
    public int[] computeEffectiveAddr(int[] instruction){
        //Formatting data from the Array
        String strInstruction = Arrays.toString(instruction);
        strInstruction = strInstruction.replace("[", "");
        strInstruction = strInstruction.replace("]", "");
        strInstruction = strInstruction.replace(",", "");
        strInstruction = strInstruction.replace(" ", "");
        
        //Calculate I
        int I;
        if (strInstruction.charAt(10) == '0'){
            I = 0;
        }else{
            I = 1;
        }
        
        //Calculate R
        int R;
        if(strInstruction.charAt(6) == '0' && strInstruction.charAt(7) == '0'){
            R = 0;
        }else if(strInstruction.charAt(6) == '0' && strInstruction.charAt(7) == '1'){
            R = 1;
        }
        else if(strInstruction.charAt(6) == '1' && strInstruction.charAt(7) == '0'){
            R = 2;
        }else{
            R = 3;
        }
        
        //Calculate IX
        int IX;
        if(strInstruction.charAt(8) == '0' && strInstruction.charAt(9) == '0'){ 
            IX = 0;
        }else if(strInstruction.charAt(8) == '0' && strInstruction.charAt(9) == '1'){
            IX = 1;
        }
        else if(strInstruction.charAt(8) == '1' && strInstruction.charAt(9) == '0'){
            IX = 2;
        }else{
            IX = 3;
        }
        
        //Calculate Address Field
        int[] Addr_Field = Arrays.copyOfRange(instruction, 11, 16);
        
        //Calculate EA using I,R,IX, and Address Field
        int EA;
        int[] tmp_var;
        if (I == 0){
            if (IX == 0){  
                //EA is the whole Address Field
                EA = binaryToInt(Addr_Field);
                
            }else {
                //Get IX Register value
                int IX_value;
                if (IX == 1){
                    IX_value = binaryToInt(getRegisterValue("X1"));
                }else if (IX == 2){
                    IX_value = binaryToInt(getRegisterValue("X2"));
                }else{
                    IX_value = binaryToInt(getRegisterValue("X2"));
                }
                EA = binaryToInt(Addr_Field) + IX_value;
                
            }
        }else{
            if (IX == 0) {
                //Get memory index from Address Field
                int tmp_var2 = binaryToInt(Addr_Field);
                
                tmp_var = getMemoryValue(tmp_var2);
                
                //Get address value from earlier address
                tmp_var2 = binaryToInt(tmp_var);
                
                tmp_var = getMemoryValue(tmp_var2);
                EA = binaryToInt(tmp_var);
                
            }else{
                //Get Value in Ix Register
                int IX_value;
                if (IX == 1){
                    IX_value = binaryToInt(getRegisterValue("X1"));
                }else if (IX == 2){
                    IX_value = binaryToInt(getRegisterValue("X2"));
                }else{
                    IX_value = binaryToInt(getRegisterValue("X2"));
                }
                
                //Get value of Address Field
                int tmp_var2 = binaryToInt(Addr_Field);
                //Add the two together to get memory location of EA
                int tmp_var3 = tmp_var2 + IX_value;
                EA = binaryToInt(getMemoryValue(tmp_var3)); 
            }
        }
        int ret[] = {EA,I,R,IX, binaryToInt(Addr_Field)};
        return ret;
    }
    
     //Function to decode a operation instruction
    public String decodeOPCode(int[] binary_OPCode){
        String ret_val;
        // Convert int array to string
        String opCode = Arrays.toString(binary_OPCode);
        opCode = opCode.replace("[", "");
        opCode = opCode.replace("]", "");
        opCode = opCode.replace(",", "");
        opCode = opCode.replace(" ", "");
        
        if ("000001".equals(opCode)){
            ret_val = "LDR";
        }else if("000010".equals(opCode)){
            ret_val = "STR";
        }else if("000011".equals(opCode)){
            ret_val = "LDA";
        }else if("100001".equals(opCode)){
            ret_val = "LDX";
        }else if("100010".equals(opCode)){
            ret_val = "STX";
        }else if("000101".equals(opCode)){
            ret_val = "STR";
        }else if("000110".equals(opCode)){
            ret_val = "STR";
        }else if("000111".equals(opCode)){
            ret_val = "STR";
        }else if("000000".equals(opCode)){
            ret_val = "HLT";
        }else{
            ret_val = "HLT";
        }
        return ret_val;
    }
    
     //Function to get values from the registers
    public int[] getRegisterValue(String register){
        int[] ret_value;
        if ("PC".equals(register)){
            ret_value = PC.getRegisterValue();
        }else if("CC".equals(register)){
            ret_value = CC.getRegisterValue();
        }else if("IR".equals(register)){
            ret_value = IR.getRegisterValue();
        }else if("MAR".equals(register)){
            ret_value = MAR.getRegisterValue();
        }else if("MBR".equals(register)){
            ret_value = MBR.getRegisterValue();
        }else if("MFR".equals(register)){
            ret_value = MFR.getRegisterValue();
        }else if("X1".equals(register)){
            ret_value = X1.getRegisterValue();
        }else if("X2".equals(register)){
            ret_value = X2.getRegisterValue();
        }else if("X3".equals(register)){
            ret_value = X3.getRegisterValue();
        }else if("GPR0".equals(register)){
            ret_value = GPR0.getRegisterValue();
        }else if("GPR1".equals(register)){
            ret_value = GPR1.getRegisterValue();
        }else if("GPR2".equals(register)){
            ret_value = GPR2.getRegisterValue();
        }else if("GPR3".equals(register)){
            ret_value = GPR3.getRegisterValue();
        }else{
            ret_value = HLT.getRegisterValue();
        }
        return ret_value;
    }
    
     //Function to set value to the registers
    public void setRegisterValue(String register, int[] value){
        if ("PC".equals(register)){
            if (binaryToInt(value)< 10){
                //If PC tries to be set less than 10, set it to 10
                int[] tmp_val = {0,0,0,0,0,0,0,0,1,0,1,0};
                PC.setRegisterValue(tmp_val);
            }else{
                PC.setRegisterValue(value);
            }
        }else if("CC".equals(register)){
            CC.setRegisterValue(value);
        }else if("IR".equals(register)){
            IR.setRegisterValue(value);
        }else if("MAR".equals(register)){
            MAR.setRegisterValue(value);
        }else if("MBR".equals(register)){
            MBR.setRegisterValue(value);
        }else if("MFR".equals(register)){
            MFR.setRegisterValue(value);
        }else if("X1".equals(register)){
            X1.setRegisterValue(value);
        }else if("X2".equals(register)){
            X2.setRegisterValue(value);
        }else if("X3".equals(register)){
            X3.setRegisterValue(value);
        }else if("GPR0".equals(register)){
            GPR0.setRegisterValue(value);
        }else if("GPR1".equals(register)){
            GPR1.setRegisterValue(value);
        }else if("GPR2".equals(register)){
            GPR2.setRegisterValue(value);
        }else if("GPR3".equals(register)){
            GPR3.setRegisterValue(value);
        }else{
            HLT.setRegisterValue(value);
        }
        
    }
    
     //Function to get a value from memory
    public int[] getMemoryValue(int row){
        if (row < 6){            
            int[] in_button_array = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
            return in_button_array;
            
        }else{
            return main_Memory.getMemoryValue(row);
        }
    }
    
     //Function to set a value to memory
    public void setMemoryValue(int row, int[] value){
        if (row < 6){
            int[] fault_code = new int[]{0,0,0,1};
            MFR.setRegisterValue(fault_code);
            int [] msg = new int[]{1};
            HLT.setRegisterValue(msg);
        }else{
            main_Memory.setMemoryValue(row, value);
        }
    }
    
     //Function to load the IPL.txt file into memeory
    public void loadFileIntoMemory() throws FileNotFoundException, IOException{
        String path_var = System.getProperty("user.dir") + "/IPL.txt";
        FileInputStream fstream = new FileInputStream(path_var);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            String[] tokens = strLine.split(" ");
            //Get the address and load into MAR
            int row = hexToInt(tokens[0]);
            int[] row_binary = hexToBinaryArrayShort(tokens[0]);
            setRegisterValue("MAR",row_binary);
            
            //Get the value and load into MBR
            int[] value = hexToBinaryArray(tokens[1]);
            setRegisterValue("MBR",value);

            if (row == 0){
                setMemoryValue(row,value);
            }else{
                setMemoryValue(row,value);
            }
        }
    }
    
    //Function to convert a hexadecimal value to integer
    public int hexToInt(String hex){
        int ret_val = Integer.parseInt(hex,16);
        return ret_val;
    }
    
    //Function to convert a binary value to hexadecimal
    public int binaryToInt(int[] binary){
        int ret_val;
        String binary_holder = Arrays.toString(binary);
        binary_holder = binary_holder.replace("[", "");
        binary_holder = binary_holder.replace("]", "");
        binary_holder = binary_holder.replace(",", "");
        binary_holder = binary_holder.replace(" ", "");
        ret_val = Integer.parseInt(binary_holder, 2);
        return ret_val;
    }
    
     //Function to convert a hexadecimal value to binary value
    public int[] hexToBinaryArray(String hex){
        int[] ret_val = new int[16];
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        
        char[] arr = hex.toCharArray();
       
        for (int i = 0; i < (hex.length()); i++) {
            ret_val[i] = Character.getNumericValue(hex.charAt(i));
        }
        return ret_val;
    }
    
     //Function to convert a binary int value to binary array value.
    public int[] intToBinaryArray(String int_value){
        int[] ret_val = new int[16];
        
        char[] arr = int_value.toCharArray();
       
        
        for (int i = 0; i < 16; i++) {
            if (i < 16 - arr.length){
                ret_val[i] = 0;
            }else{
                ret_val[i] = Character.getNumericValue(int_value.charAt(i-(16 - arr.length)));
            }
            
        }
        return ret_val;
    }
    
     //Function to convert a binary value to binary array value specifically for the MAR register.
    public int[] hexToBinaryArrayShort(String hex){
        int[] ret_val = new int[12];
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        
        char[] arr = hex.toCharArray();
       
        
        for (int i = 0; i < (hex.length()-4); i++) {
            ret_val[i] = Character.getNumericValue(hex.charAt(i+4));
        }
        return ret_val;
    }
    
     //Function to convert a binary int value to binary array value specifically for the PC
    public int[] intToBinaryArrayShort(String int_value){
        int[] ret_val = new int[12];
        
        char[] arr = int_value.toCharArray();
       
        
        for (int i = 0; i < 12; i++) {
            if (i < 12 - arr.length){
                ret_val[i] = 0;
            }else{
                ret_val[i] = Character.getNumericValue(int_value.charAt(i-(12 - arr.length)));
            }
            
        }
        return ret_val;
    }
}
