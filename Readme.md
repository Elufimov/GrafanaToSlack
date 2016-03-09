# Grafana to Slack integration
 
 This project represent small integration service between [Grafana](http://grafana.org/), [Slack](https://slack.com/) and [Elastic Watchers](https://www.elastic.co/products/watcher). But because watchers communicate with this service through http it could be considered as universal integration between grafana and slack. 
  
## Quick start with docker
 
 Assume that you have docker machine up and running.
 
 1) Start postgres container 
 ```
 docker run --name -p 5432:5432 some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres
 ```
 2) Start GrafanaToSlack
```bash
docker run -d -p 9000:9000 --link postgres:postgres elufimov/grafanatoslack:0.1 \
 -Desgs.grafanaToken=Accsess_token_from_grafana \
 -Desgs.homeDir=/opt/docker \
 -Desgs.grafanaHost=Grafana_url \
 -Desgs.slackToken=Slack_token \
 -Desgs.publicUrl=Public_url_of_GrafanaToSlack \
 -Dplay.evolutions.db.default.autoApply=true \
 -Dslick.dbs.default.db.url="jdbc:postgresql://ip_of_docker_demon_vm:5432/postgres" \
 -Dslick.dbs.default.db.user="postgres" \
 -Dslick.dbs.default.db.password="mysecretpassword"
```

* esgs.grafanaToken can be found at http://mygrafana/org/apikeys
* esgs.grafanaHost same as previous http://mygrafana
* esgs.slackToken https://hooks.slack.com/services/xxxxxxxxx/xxxxxxxxx/xxxxxxxxxxxxxxxxxxxxxxx
* esgs.publicUrl public url with port for stored images. MUST be public for slack image preview
* ip_of_docker_demon_vm can be found by command `docker-machine ip vm_name`

## Usage 

Incoming json format similar with slack incoming hooks json as describe in slack documentation https://api.slack.com/incoming-webhooks https://api.slack.com/docs/attachments Only difference is that `channel` is a `List[String]`. After message received service will scan all attaches for url containing text in `esgs.grafanaHost`, if match found service will download image from it and replace original url. Then message will be send to all users in `channel` list.

## Example
curl
```bash
curl -X "POST" "http://192.168.99.100:81/forward" \
	-H "Content-Type: application/json" \
	-d "{\"channels\":[\"@m.elufimov\"],\"username\":\"watcher\",\"attachments\":[{\"image_url\":\"http://play.grafana.org/render/dashboard-solo/db/annotations?panelId=1\",\"text\":\"text\",\"title\":\"title\"}],\"text\":\"\"}"
```
Elastic Watcher action
```json
"actions": {
    "my_webhook": {
      "webhook": {
        "method": "POST",
        "host": "grafanaToSlack",
        "port": 80,
        "path": "/forward",
        "body": "{\"channels\":[\"@m.elufimov\"],\"username\":\"watcher\",\"attachments\":[{\"image_url\":\"http://play.grafana.org/render/dashboard-solo/db/annotations?panelId=1\",\"text\":\"text\",\"title\":\"title\"}],\"text\":\"\"}",
        "headers" : {
          "Content-Type" : "application/json"
        }
      }
    }
  }

```

## License

The MIT License (MIT)

Copyright (c) 2015 Michael Elufimov

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

