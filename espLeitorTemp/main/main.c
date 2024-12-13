#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "driver/gpio.h"
#include "dht.h"                    
#include "esp_log.h"
#include "esp_event.h"
#include "nvs_flash.h"
#include "esp_netif.h"
#include "esp_wifi.h"
#include "mqtt_client.h"

#define MQTT_BROKER_URI "mqtt://192.168.0.101:1884"
#define MQTT_TOPIC "esp32"

#define ROW_1 GPIO_NUM_33 // 4
#define ROW_2 GPIO_NUM_27 // 3
#define ROW_3 GPIO_NUM_26 // 2
#define ROW_4 GPIO_NUM_32 // 1
#define COL_1 GPIO_NUM_18 // 5
#define COL_2 GPIO_NUM_19 // 6
#define COL_3 GPIO_NUM_21 // 7
#define COL_4 GPIO_NUM_23 // 8

#define DHT_GPIO 4
#define SENSOR_TYPE DHT_TYPE_AM2301

#define LEDPIN GPIO_NUM_5      // led externo vermelho, simula a ventoinha
#define LED_GPIO_PIN GPIO_NUM_2 // led interno azul, simula o sprinkler



#define LOG_TAG "TEMPO"

static const char *MATRIX_TAG = "TempUmidade";

gpio_num_t colPins[4] = {ROW_1, ROW_2, ROW_3, ROW_4};
gpio_num_t rowPins[4] = {COL_1, COL_2, COL_3, COL_4};

static const char *ESP32_TOPIC = "esp32";

char matrix[4][4] = {{'1', '2', '3', 'A'},
                     {'4', '5', '6', 'B'},
                     {'7', '8', '9', 'C'},
                     {'0', '0', '0', 'D'}};

char tempMax[3]="20";
char umidadeMax[3]="20";

bool digiTemp = false;
bool digitUmi = false;
bool ventoinhaAtivada=false;

int intervalo= 20;
int TAMTEMPUM=2;
int maxDigitador=0;
int posicaoChar=0;
int posicaoStr = 0;

int tempoUpdate=0;
char newChar[80];

char tempAtual[3] = "10";

char umidadeAtual[3] = "10";

esp_mqtt_client_handle_t mqtt_client = NULL;

void mqtt_publish_message(esp_mqtt_client_handle_t client, const char *message)
{
    int msg_id = esp_mqtt_client_publish(client, "test", message, 0, 1, 0);
    ESP_LOGI(MATRIX_TAG, "Mensagem enviada, ID: %d", msg_id);
}
//sprinkler 
void piscapisca(esp_mqtt_client_handle_t client, int num)
{
    for (int contador = num; contador > 0; contador--)
    {
        gpio_set_level(LED_GPIO_PIN, 1);
        vTaskDelay(500 / portTICK_PERIOD_MS);
        mqtt_publish_message(client,"01AI");
        gpio_set_level(LED_GPIO_PIN, 0);
        vTaskDelay(500 / portTICK_PERIOD_MS);
        mqtt_publish_message(client,"00AI");
        ESP_LOGI(MATRIX_TAG, "BLINK");
    }
}

void ligarVentoinha(esp_mqtt_client_handle_t client,bool ligar){
    
    if(ligar==true){
        ESP_LOGI(MATRIX_TAG,"LIGANDO A VENTOINHA...");
        gpio_set_level(LEDPIN,1);
        ventoinhaAtivada=true;
        mqtt_publish_message(client,"01AV");

    }else if(ligar==false){
        ESP_LOGI(MATRIX_TAG,"DESLIGANDO A VENTOINHA...");
        gpio_set_level(LEDPIN,0);
        ventoinhaAtivada=false;
        mqtt_publish_message(client,"00AV");

    }
}

static void mqtt_event_handler_cb(void *handler_args, esp_event_base_t base, int32_t event_id, void *event_data)
{
    esp_mqtt_event_handle_t event = event_data;
    esp_mqtt_client_handle_t client = event->client;

    switch (event->event_id)
    {
    case MQTT_EVENT_CONNECTED:
        ESP_LOGI(MATRIX_TAG, "MQTT_EVENT_CONNECTED");
        esp_mqtt_client_subscribe(client, ESP32_TOPIC, 1);
        ESP_LOGI(MATRIX_TAG, "Subscribed to topic: %s", ESP32_TOPIC);
        break;
    case MQTT_EVENT_DISCONNECTED:
        ESP_LOGI(MATRIX_TAG, "MQTT_EVENT_DISCONNECTED");
        break;
    case MQTT_EVENT_DATA:
        ESP_LOGI(MATRIX_TAG, "Message received on topic: %.*s", event->topic_len, event->topic);
        ESP_LOGI(MATRIX_TAG, "Message content: %.*s", event->data_len, event->data);

        if (strncmp(event->topic, ESP32_TOPIC, event->topic_len) == 0)
        {
            char received_message[256];
            snprintf(received_message, event->data_len + 1, "%.*s", event->data_len, event->data);

            if (strlen(received_message) >= 3)
            {
                char tresu[4];
                char novodado[3];
                strncpy(tresu, &received_message[strlen(received_message) - 2], 2);
                strncpy(novodado, received_message, 2);
                novodado[2]='\0';
                tresu[3] = '\0';
                ESP_LOGI(MATRIX_TAG,"%s",tresu);
                //TEmperatura, UMidade, TIme pq n tinha como colocar TEmpo 
                if (strcmp(tresu, "TE") == 0)
                {
                    ESP_LOGI(MATRIX_TAG,"%s",novodado);
                    strncpy(tempMax,novodado,3);
                }
                else if (strcmp(tresu, "UM") == 0)
                {
                    ESP_LOGI(MATRIX_TAG,"%s",novodado);
                    strncpy(umidadeMax,novodado,3);
                }
                else if (strcmp(tresu, "TI") == 0)
                {
                    ESP_LOGI(MATRIX_TAG,"%s",novodado);
                    float min=atof(novodado);
                    if(min>0){
                        intervalo=min*60;
                    }
                }else if(strcmp(tresu,"VA")==0){
                    ESP_LOGI(MATRIX_TAG,"Ativando sprinklers");
                    piscapisca(client,1);
                }
                else
                {
                    ESP_LOGI(MATRIX_TAG, "???????????: %s", received_message);
                }
            }
            else
            {
                ESP_LOGI(MATRIX_TAG, "Mensagem muito curta: %s", received_message);
            }
        }
        break;
    case MQTT_EVENT_PUBLISHED:
        ESP_LOGI(MATRIX_TAG, "Mensagem publicada com sucesso, ID: %d", event->msg_id);
        break;
    case MQTT_EVENT_ERROR:
        ESP_LOGE(MATRIX_TAG, "MQTT_EVENT_ERROR");
        break;
    default:
        ESP_LOGI(MATRIX_TAG, "Outro evento MQTT, ID: %d", event->event_id);
        break;
    }
}

esp_mqtt_client_handle_t mqtt_app_start()
{
    esp_mqtt_client_config_t mqtt_cfg = {
        .broker.address.uri = MQTT_BROKER_URI,
    };
    esp_mqtt_client_handle_t client = esp_mqtt_client_init(&mqtt_cfg);
    esp_mqtt_client_register_event(client, ESP_EVENT_ANY_ID, &mqtt_event_handler_cb, client);
    esp_mqtt_client_start(client);
    return client;
}

static void wifi_event_handler(void *arg, esp_event_base_t event_base, int32_t event_id, void *event_data)
{
    if (event_id == WIFI_EVENT_STA_START)
    {
        esp_wifi_connect();
    }
    else if (event_id == WIFI_EVENT_STA_DISCONNECTED)
    {
        esp_wifi_connect();
        ESP_LOGI(MATRIX_TAG, "Tentando reconectar ao Wi-Fi...");
    }
    else if (event_id == IP_EVENT_STA_GOT_IP)
    {
        ESP_LOGI(MATRIX_TAG, "Conectado ao Wi-Fi, endereço IP obtido.");
    }
}

void wifi_init()
{
    esp_netif_init();
    esp_event_loop_create_default();
    esp_netif_create_default_wifi_sta();
    wifi_init_config_t cfg = WIFI_INIT_CONFIG_DEFAULT();
    esp_wifi_init(&cfg);
    esp_event_handler_register(WIFI_EVENT, ESP_EVENT_ANY_ID, &wifi_event_handler, NULL);
    esp_event_handler_register(IP_EVENT, IP_EVENT_STA_GOT_IP, &wifi_event_handler, NULL);
    wifi_config_t wifi_config = {
        .sta = {
            .ssid = "virus.esp",
            .password = "176466754",
        },
    };
    esp_wifi_set_mode(WIFI_MODE_STA);
    esp_wifi_set_config(ESP_IF_WIFI_STA, &wifi_config);
    esp_wifi_start();
}

void concatena(int x, int y)
{
    if(digiTemp || digitUmi){
        if(TAMTEMPUM<2){
            newChar[posicaoStr]=matrix[x][y];
            posicaoStr++;
            newChar[posicaoStr+1]='\0';
        }
    }
}

void digitar()
{   
    
    ESP_LOGI(MATRIX_TAG,"digite:");
    if (posicaoStr > maxDigitador)
    {
        int i = strlen(newChar);
        for (int x = i; x > 0; x--)
        {
            newChar[x] = '\0';
        }
        posicaoStr = 0;
    }

    while (posicaoStr< maxDigitador)
    {

        for (int x = 0; x < 4; x++)
        {

            gpio_set_level(rowPins[x], 0);

            for (int y = 0; y < 3; y++)
            {

                if (gpio_get_level(colPins[y]) == 0)
                {

                    concatena(x, y);
                    vTaskDelay(100 / portTICK_PERIOD_MS);
                
                }
            }
            gpio_set_level(rowPins[x], 1);
        }
        vTaskDelay(100 / portTICK_PERIOD_MS);
    }
    ESP_LOGI(MATRIX_TAG,"Nova Umidade:%s",umidadeAtual);
}

void funcao(esp_mqtt_client_handle_t client, int contador)
{   
    ESP_LOGI(MATRIX_TAG,"ASDASD");
    switch (contador)
    {
    case 1:
        digiTemp=true; //digitar uma nova temperatura por meio da matriz de botoes
        maxDigitador=TAMTEMPUM;
        digitar();
        break;
    case 2:
        digitUmi=true; //digitar uma nova umidade por meio da matriz de botoes
        maxDigitador=TAMTEMPUM;
        digitar();
        break;
    case 3:
        piscapisca(client,1); //teste do led
        break;
    default:
        break;
    }
}
void lerTemp(esp_mqtt_client_handle_t client) {
    int16_t humidity = 0;
    int16_t temperature = 0;
    esp_err_t result = dht_read_data(SENSOR_TYPE, DHT_GPIO, &humidity, &temperature);

    if (result == ESP_OK) {
        int umidade= humidity / 10.0;
        int temperaturaC = temperature / 10.0;
        char temperaturamsg[64];

	    sprintf(tempAtual,"%d",temperaturaC);
        sprintf(umidadeAtual,"%d",umidade);
        //adiciona as tags pro java se orientar
        snprintf(temperaturamsg, sizeof(temperaturamsg), "%dTE%dUM", 
                 temperaturaC, umidade);
        //envia a msg
        mqtt_publish_message(client, temperaturamsg);

        ESP_LOGI(MATRIX_TAG, "Mensagem enviada: %s", temperaturamsg);
    } else {
        ESP_LOGE(MATRIX_TAG, "Erro ao ler o sensor DHT: %s", esp_err_to_name(result));
    }
}
void app_main(void)
{   
    TickType_t tempoInicial = xTaskGetTickCount();
    TickType_t tempoNovo;
    int tempoInicial40s=0;
        
    nvs_flash_init();
    wifi_init();
    esp_mqtt_client_handle_t client = mqtt_app_start();

    gpio_config_t io_conf = {
        .pin_bit_mask = (1ULL << LEDPIN) | (1ULL << LED_GPIO_PIN),
        .mode = GPIO_MODE_OUTPUT,
        .pull_up_en = GPIO_PULLUP_DISABLE,
        .pull_down_en = GPIO_PULLDOWN_DISABLE,
        .intr_type = GPIO_INTR_DISABLE
    };
    gpio_config(&io_conf);

    gpio_reset_pin(LED_GPIO_PIN);
    gpio_set_direction(LED_GPIO_PIN, GPIO_MODE_OUTPUT);
    for (int i = 0; i < 4; i++)
    {
        gpio_set_direction(rowPins[i], GPIO_MODE_OUTPUT);
        gpio_set_level(rowPins[i], 1);
    }

    for (int i = 0; i < 4; i++)
    {
        gpio_set_direction(colPins[i], GPIO_MODE_INPUT);
        gpio_set_pull_mode(colPins[i], GPIO_PULLUP_ONLY);
    }
    ESP_LOGI(MATRIX_TAG,"Botoes configurados da seguinte maneira:");
    ESP_LOGI(MATRIX_TAG,"Função A para digitar uma nova temperatura maxima");
    ESP_LOGI(MATRIX_TAG,"Função B para digitar uma nova umidade maxima");
    for(int x=0;x>4;x++){
        for(int y=0;y<4;y++){
            ESP_LOGI(MATRIX_TAG,"%d",matrix[x][y]);
        }
    }

    while (1)
    {
        for (int x = 0; x < 4; x++)
        {
            gpio_set_level(rowPins[x], 0);
            int y = 3;
            if (gpio_get_level(colPins[y]) == 0)
            {
                int local = x * 4 + y + 1;
                int env = local / 4;

                funcao(client,env);

                vTaskDelay(100 / portTICK_PERIOD_MS);
            }

            gpio_set_level(rowPins[x], 1);
        }
        tempoNovo = xTaskGetTickCount();
        if (tempoNovo - tempoInicial > pdMS_TO_TICKS(intervalo*1000)) { 
            
            tempoInicial = tempoNovo;
            piscapisca(client,1);
        }
        if (tempoNovo - tempoInicial40s > pdMS_TO_TICKS(40000)) {
            if(atoi(tempAtual)>=atoi(tempMax) && ventoinhaAtivada==false){    
               ligarVentoinha(client,true);
            }else if(atoi(tempAtual)<=atoi(tempMax)&&ventoinhaAtivada==true){
                ligarVentoinha(client,false);
            }
            tempoInicial40s = tempoNovo;
        }
        

        vTaskDelay(300 / portTICK_PERIOD_MS);
    }
}
