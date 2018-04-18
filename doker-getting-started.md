# 도커 시작하기

## 참고

[도커 시작하기](https://docs.docker.com/get-started/)
[유툽/백기선/도커](https://www.youtube.com/playlist?list=PLfI752FpVCS84hxOeCyI4SBPUwt4Itd0T)

## 유툽 인덱스

[![도커 시작하기 1](https://img.youtube.com/vi/9tW0QSsrhwc/0.jpg)](https://youtu.be/9tW0QSsrhwc)

파트 1에서는 도커에 대한 기본적인 설명과 특징을 살펴봤습니다. 

파트 2에서는 Dockerfile을 사용해서 간단한 파이썬 이미지를 만들었고, docker run을 사용해서 컨테이너를 실행하고, 이미지를 태깅하고, 도커 레지스트리에 등록하는 것까지 따라해 보았습니다.

[![도커 시작하기 2](https://img.youtube.com/vi/p58k2_HMWRM/0.jpg)](https://youtu.be/p58k2_HMWRM)

오늘은 시작하기 파트3 과 파트4를 봤습니다. 도커의 서비스라는 개념을 살펴봤으며 docker-compose.yml 파일을 사용해서 간단히 서비스를 정의하고 실행해 봤습니다.

파트3에서 실행할 때는 로컬 머신에서 도커 스왐을 만들고 docker stack deploy를 사용했는데, 파트 4에서는 VM을 두개 만들어서 본격적으로 스왐을 구성합니다.

myvm1은 스왐 매니저로 만들고 myvm2는 워커로 만들었습니다. 스왐 매니저만 도커 명령을 실행할 수 있기 때문에 매번 docker-machine ssh myvm1 "docker ..." 이런식으로 도커를 실행해야 하는 번거로움이 있는데, docker-machine env 를 사용해서 그런 수고를 덜 수 있는 방법을 살펴봤습니다. (그런데 간혹 로컬에 있는 도커랑 햇갈릴듯..)

혹시나 맥에서 VirtualBox 설치할 때 문제 있으신 분들은 아래 링크 참고하셔서 해결하시기 바랍니다. 혹은 제 영상 보시면 간략히 설명해 드렸으니까 참고하세요.

https://stackoverflow.com/questions/46546192/virtualbox-not-installing-on-high-sierra

[![도커 시작하기 3](https://img.youtube.com/vi/X--WPFfSbFc/0.jpg)](https://youtu.be/X--WPFfSbFc)

마지막은 이전까지 만들었던 docker-compose.yml에 서비스를 두개 더 추가하고 AWS나 Azure에 배포하는 방법을 소개하고 있습니다.

기존 서비스와는 독립적인 서비스 (컨테이너 배포 상황을 보여주는 이미지)와 기존 서비스가 사용하는 서비스 (레디스)를 각각 추가해서 동작하는걸 데모로 해봤습니다. 중간에 맥 쓰시는 분들은 아마도 저처럼 문제(비주얼라이저 컨테이너가 잘 안뜨는)가 생길 수 있을텐데요. 영상에서 해결했사오니 궁금하신 분들은 확인해 보세요.

아쉽게도 6부 AWS나 Azure에 배포하는건 따라해보지 못했습니다. 차마 그쪽까지 세팅할만큼 부지런하질 못해서... 대신 개념적으로만 살펴봤는데요. 결국엔 AWS든 Azure든 도커 스왐으로 묶어서 docker-compose.yml 파일을 배포하는건 똑같더라구요. 대신 손만 조금 더 많이 가게 생겼더군요.

각 서비스 계정 열어야지, 도커 인스턴스 만들어야지, 스왐으로 연결 해야지, 컨테이너 관련된 포트를 열어줘야지..  @_@..

