// Script principal do site Eco Ponto

document.addEventListener('DOMContentLoaded', function() {
    // Modal para detalhes do ecoponto
    const modal = document.getElementById('ecoponto-modal');
    const closeModal = document.getElementById('close-modal');
    
    // Botão de busca de ecopontos
    const searchBtn = document.getElementById('search-btn');
    
    if (searchBtn) {
        searchBtn.addEventListener('click', function() {
            const searchInput = document.getElementById('search-input');
            if (searchInput && searchInput.value.trim() !== '') {
                // Redirecionar para a página de resultados com o termo de busca
                window.location.href = `/ecopontos/resultado?q=${encodeURIComponent(searchInput.value.trim())}`;
            } else {
                alert('Por favor, digite um CEP ou endereço válido.');
            }
        });
    }
    
    // Fechar modal quando clicar no X
    if (closeModal && modal) {
        closeModal.addEventListener('click', function() {
            modal.style.display = 'none';
        });
    }
    
    // Fechar modal quando clicar fora dele
    if (modal) {
        window.addEventListener('click', function(event) {
            if (event.target === modal) {
                modal.style.display = 'none';
            }
        });
    }
    
    console.log('Script do Eco Ponto carregado com sucesso!');
}); 