document.getElementById('shiftButton').addEventListener('click', function() {
    const col = document.querySelector('.col');
    const modal = document.getElementById('modal');
    const modalContent = document.querySelector('.modal-content');

    col.style.transform = 'translateX(-600px)';

    modal.style.display = 'block';
    setTimeout(() => {
        modal.style.transform = 'translateX(300px)';
        modal.style.opacity = '1';
        modalContent.style.transform = 'translateX(0)';
        modalContent.style.opacity = '1';
    }, 10);
});

document.querySelector('.close-modal').addEventListener('click', function() {
    const col = document.querySelector('.col');
    const modal = document.getElementById('modal');
    const modalContent = document.querySelector('.modal-content');


    col.style.transform = 'translateX(0)';


    modal.style.transform = 'translateX(-50%)';
    modal.style.opacity = '0';
    modalContent.style.transform = 'translateX(100%)';
    modalContent.style.opacity = '0';


    setTimeout(() => {
        modal.style.display = 'none';
    }, 1500);
});

document.getElementById('shiftButton2').addEventListener('click', function() {
    const col2 = document.querySelector('.col2');
    const modal2 = document.getElementById('modal2');
    const modalContent2 = document.querySelector('.modal-content2');

    col2.style.transform = 'translateX(-600px)';

    modal2.style.display = 'block';
    setTimeout(() => {
        modal2.style.transform = 'translateX(300px)';
        modal2.style.opacity = '1';
        modalContent2.style.transform = 'translateX(0)';
        modalContent2.style.opacity = '1';
    }, 10);
});

document.querySelector('.close-modal2').addEventListener('click', function() {
    const col2 = document.querySelector('.col2');
    const modal2 = document.getElementById('modal2');
    const modalContent2 = document.querySelector('.modal-content2');


    col2.style.transform = 'translateX(0)';


    modal2.style.transform = 'translateX(-50%)';
    modal2.style.opacity = '0';
    modalContent2.style.transform = 'translateX(100%)';
    modalContent2.style.opacity = '0';


    setTimeout(() => {
        modal2.style.display = 'none';
    }, 1500);
});

document.getElementById('shiftButton3').addEventListener('click', function() {
    const col3 = document.querySelector('.col3');
    const modal3 = document.getElementById('modal3');
    const modalContent3 = document.querySelector('.modal-content3');

    col3.style.transform = 'translateX(-600px)';

    modal3.style.display = 'block';
    setTimeout(() => {
        modal3.style.transform = 'translateX(300px)';
        modal3.style.opacity = '1';
        modalContent3.style.transform = 'translateX(0)';
        modalContent3.style.opacity = '1';
    }, 10);
});

document.querySelector('.close-modal3').addEventListener('click', function() {
    const col3 = document.querySelector('.col3');
    const modal3 = document.getElementById('modal3');
    const modalContent3 = document.querySelector('.modal-content3');


    col3.style.transform = 'translateX(0)';


    modal3.style.transform = 'translateX(-50%)';
    modal3.style.opacity = '0';
    modalContent3.style.transform = 'translateX(100%)';
    modalContent3.style.opacity = '0';


    setTimeout(() => {
        modal.style.display = 'none';
    }, 1500);
});