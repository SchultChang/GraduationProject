/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graduationproject.controllers;

/**
 *
 * @author cloud
 */
public class InterfaceManagementController {
    
    public enum DataOrders {
        IP_ADDRESS(0),
        NETMASK(1), 
        MTU(2),
        BANDWIDTH(3),
        IN_PACK_NUMBER(4),
        OUT_PACK_NUMBER(5),
        IN_BYTES(6),
        OUT_BYTES(7), 
        IN_ERR_PACK_NUMBER(8), 
        OUT_ERR_PACK_NUMBER(9),
        NEXT_NODE_NAME(10), 
        NEXT_NODE_LABEL(11),
        NEXT_NODE_IP_ADDRESS(12),
        NEXT_NODE_MAC_ADDRESS(13);
        
        private int value;
        private DataOrders(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return this.value;
        }
    }
}
