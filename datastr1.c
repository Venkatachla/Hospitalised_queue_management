#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_STAFF 5
#define MAX_PATIENTS 100

typedef struct {
    char name[15];
    char password[15];
} Staff;

Staff staff_account[MAX_STAFF] = {
    {"admin", "adminpass"},
    {"Vijay_VP", "vijayvp"},
    {"Venkatachala", "venky"},
    {"Vijay", "vijay"},
    {"Veeresh", "veeresh"}
};

typedef enum DiseaseSeverity{
    Headache = 1,
    Viral_Fever,
    Infection,         
    Diabetes_Checkup,
    Fracture,
    Chest_Pain,
    Cancer,
    Severe_Wound,
    Heart_Attack,
    Stroke,
} DiseaseSeverity;

const char* diseasesNames(DiseaseSeverity Severity)  {
    switch(Severity){
        case 1: return "Headache";
        case 2: return "Viral Fever";
        case 3: return "Infection";
        case 4: return "Diabetes Checkup";
        case 5: return "Fracture";
        case 6: return "Chest pain";
        case 7: return "Cancer";
        case 8: return "Severe wound";
        case 9: return "Heart attack";
        case 10: return "Stroke";
        default: return "Unknown Disease";
    }  
};

typedef enum {
    Male = 0,
    Female,
    Other
} Sex;

const char* getSexString(Sex sex) {
    switch (sex) {
        case 0: return "Male";
        case 1: return "Female";
        case 2: return "Other";
        default: return "Unknown";
    }
}

typedef struct node *nodePointer;
typedef struct node {
    char name[50];
    int age;
    Sex sex;
    char address[100];
    long int mobile_no;
    int id;
    DiseaseSeverity severity;
    int waiting_time;
    int urgency;
    float priority;
    nodePointer link;
} node;

nodePointer start = NULL;
int current_id = 1;
float w1 = 0.5, w2 = 0.3, w3 = 0.2;

int login();
float calculatePriority(nodePointer *p);
void addPatient(nodePointer *start);
void treatPatients(nodePointer *start);
void sort_queue();
void hospitalManagement();
void view_queue(nodePointer start);


void table();

int main() {
    printf("Welcome to Hospital Queue Management System\n");
    printf("Staff Login\n");
    if (login()) {
        printf("Login Successful\n");
        hospitalManagement();
    } else {
        printf("Login Failed. Exiting the system\n");
    }
    return 0;
}

int login() {
    char name[15], password[15];
    printf("Enter username: ");
    scanf("%s", name);
    printf("Enter password: ");
    scanf("%s", password);

    for (int i = 0; i < MAX_STAFF; i++) {
        if (strcmp(name, staff_account[i].name) == 0 && strcmp(password, staff_account[i].password) == 0) {
            return 1;
        }
    }
    printf("Invalid Credentials.\n");
    return 0;
}

void hospitalManagement() {
    int choice;
    while (1) {
        printf("\nMenu\n");
        printf("1-Add Patient\n2-View Queue\n3-Treat Patient\n4-Exit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);
        switch (choice) {
            case 1:
                addPatient(&start);
                break;
            case 2:
                view_queue(start);
                break;
            case 3:
                treatPatients(&start);
                break;
            case 4:
                printf("Exiting the system\n");
                exit(0);
        }
    }
}

float calculatePriority(nodePointer *p) {
    return w1 * (*p)->severity + w2 * (*p)->waiting_time + w3 * (*p)->urgency;
}

void addPatient(nodePointer *start) {
    nodePointer patient = (nodePointer)malloc(sizeof(node));
    if(patient == NULL) {
        fprintf(stderr,"Memory Allocation Failed\n");
        exit(1);
    }
    patient->link = NULL;
    patient->id = current_id++;
    printf("\nEnter name: ");
    scanf("%s", patient->name);

    printf("Enter age: ");
    scanf("%d", &patient->age);

    printf("Select Gender (0: Male, 1: Female, 2: Other): ");
    scanf("%d", (int*)&patient->sex);

    printf("Enter address: ");
    scanf(" %[^\n]", patient->address);

    printf("Enter mobile number: ");
    scanf("%ld", &patient->mobile_no);
    table();

    printf("Select Disease Type (in number): ");
    scanf("%d", (int*)&patient->severity);
    while(patient->severity<1 || patient->severity>10) {
        printf("Invalid number. Enter valid number (1-10): ");
        scanf("%d", (int*)&patient->severity);
    }

    printf("Enter Waiting Time (in minutes): ");
    scanf("%d", &patient->waiting_time);

    printf("Enter urgency (1-10): ");
    scanf("%d", &patient->urgency);
    while (patient->urgency < 1 || patient->urgency > 10) {
        printf("Invalid urgency. Enter a value between 1 and 10: ");
        scanf("%d", &patient->urgency);
    }

    patient->priority = calculatePriority(&patient);
    nodePointer temp;
    if(*start == NULL) {
        *start = patient;
    }
    else {
        temp = *start;
        while (temp->link != NULL) {
            temp = temp->link;
        }
        temp->link = patient;
    }
    sort_queue();

    printf("\nPatient added successfully.\nPatient ID: %d\n", patient->id);
}


void view_queue(nodePointer start) {
    nodePointer ptr = start;
    if (start == NULL) {
        printf("\nThere are no patients in the queue\n");
        return;
    }
    printf("\nPriority wise appointments:\n");
    while (ptr != NULL) {
        printf("Name: %s\nAge: %d\nAddress: %s\nPhone: %ld\nPriority: %.2f\n", ptr->name, ptr->age, ptr->address, ptr->mobile_no, ptr->priority);
        printf("---------------------------------------------------\n");
        ptr = ptr->link;
    }
}

void treatPatients(nodePointer *start) {
    if (*start == NULL) {
        printf("No patients to treat\n");
        return;
    }

    nodePointer temp = *start;
    printf("Treating patient: %s (ID: %d)\n", temp->name, temp->id);
    FILE *file = fopen("output.csv", "a");

    if (file == NULL) {
        printf("Error opening file!\n");
        return;
    }
    static int count=0;
    // Write column headers if the file is empty
    // if (count == 0) {
    //     fprintf(file, "Name, Age, Gender, Address, Phone Number, Disease\n");
    //     count++;
    // }

    // Write patient details to file
    fprintf(file, "%s, %d, %s, %s, %ld, %s\n", temp->name, temp->age, getSexString(temp->sex), temp->address, temp->mobile_no, diseasesNames(temp->severity));

    // Close the file
    fclose(file);
    *start = temp->link;
    free(temp);
    sort_queue();
}
void sort_queue() {
    if (start == NULL || start->link == NULL) {
        return;  // No need to sort if the list is empty or has only one element
    }

    nodePointer sorted = NULL;  // This will hold the sorted list
    nodePointer current = start;  // Start from the original unsorted list

    // Iterate through the unsorted list and insert each node into the sorted list
    while (current != NULL) {
        nodePointer next_node = current->link;  // Save the next node, because we will modify the 'current' node

        // Insert 'current' node into the sorted list
        if (sorted == NULL || current->priority > sorted->priority) {
            current->link = sorted;  // Point 'current' node to the previous sorted list
            sorted = current;  // Make 'current' the new head of the sorted list
        } else {
            nodePointer temp = sorted;
            while (temp->link != NULL && temp->link->priority >= current->priority) {
                temp = temp->link;  // Traverse to the right spot based on priority
            }
            current->link = temp->link;  // Insert 'current' node after 'temp'
            temp->link = current;  // Update the 'temp' node's next pointer to point to 'current'
        }
        current = next_node;  // Move to the next node in the unsorted list
    }
    start = sorted;  // Set the 'start' pointer to the head of the sorted list
}

void table() {
    printf("----Please Refer this Table for your disease!\n");
    printf("1. Headache\n");
    printf("2. Viral Fever\n");
    printf("3. Infection\n");
    printf("4. Diabetes Checkup\n");
    printf("5. Fracture\n");
    printf("6. Chest pain\n");
    printf("7. Cancer\n");
    printf("8. Severe wound\n");
    printf("9. Heart attack\n");
    printf("10. Stroke\n");
}